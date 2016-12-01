package com.mabrouk.moviedb.people.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.DataBag;
import com.mabrouk.moviedb.common.WebviewActivity;
import com.mabrouk.moviedb.network.MediaUrlBuilder;
import com.mabrouk.moviedb.people.Person;
import com.mabrouk.moviedb.people.api.PeopleServiceProvider;
import com.squareup.picasso.Picasso;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class PersonDetailsActivity extends AppCompatActivity {
    private static final String EXTRA_PERSON_ID = "person_id";

    public static void startInstance(Context context, Person p) {
        DataBag.addPersonToPocket(p);
        Intent intent = new Intent(context, PersonDetailsActivity.class);
        intent.putExtra(EXTRA_PERSON_ID, p.getId());
        context.startActivity(intent);
    }

    Person person;
    int personId;
    ImageView profilePic;
    TextView name;
    ProgressBar progressBar;
    TextView deathDate;
    TextView birthDate;
    ViewPager viewPager;

    Subscription subscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);
        getSupportActionBar().hide();
        personId = getIntent().getExtras().getInt(EXTRA_PERSON_ID);
        person = DataBag.getPersonFromPocket(personId);

        profilePic = (ImageView) findViewById(R.id.profile_pic);
        name = (TextView) findViewById(R.id.name);
        birthDate = (TextView) findViewById(R.id.birth_date);
        deathDate = (TextView) findViewById(R.id.death_date);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        name.setText(person.getName());
        int profileDimen = (int) getResources().getDimension(R.dimen.person_large_thumb_dimen);
        String profileUrl = new MediaUrlBuilder(person.getProfilePath())
                .addType(MediaUrlBuilder.TYPE_PROFILE)
                .addSize(profileDimen, profileDimen)
                .build();

        Picasso.with(this).load(profileUrl).into(profilePic);

        subscription = PeopleServiceProvider.getService().getPersonDetails(personId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::gotPerson, this::gotError);
    }

    void gotPerson(Person p) {
        progressBar.setVisibility(View.GONE);
        this.person.populateFrom(p);
        birthDate.setText("Birth Date: " + person.getBirthDate());
        if (!person.getDeathDate().isEmpty())
            deathDate.setText("Death Date: " + person.getDeathDate());
        viewPager.setVisibility(View.VISIBLE);
        viewPager.setAdapter(new ViewPagerAdapter(getLayoutInflater(), person));

        displayLinks();
    }

    private void displayLinks() {
        View facebookButton = findViewById(R.id.facebook_icon);
        View twitterButton = findViewById(R.id.twitter_icon);
        View instagramButton = findViewById(R.id.instagram_icon);
        View imdbButton = findViewById(R.id.imdb_icon);
        View homepageButton = findViewById(R.id.homepage);

        ExternalIds externalIds = person.getExternalIds();
        if (externalIds.hasFacebook()) {
            facebookButton.setVisibility(View.VISIBLE);
            facebookButton.setOnClickListener(view ->
                    openExternal(externalIds.getFacebookUrl(), person.getName() + "'s Facebook"));
        }

        if (externalIds.hasTwitter()) {
            twitterButton.setVisibility(View.VISIBLE);
            twitterButton.setOnClickListener(view ->
                    openExternal(externalIds.getTwitterUrl(), person.getName() + "'s Twitter"));
        }

        if (externalIds.hasInstagram()) {
            instagramButton.setVisibility(View.VISIBLE);
            instagramButton.setOnClickListener(view ->
                    openExternal(externalIds.getInstagramUrl(), person.getName() + "'s Instagram"));
        }

        if (person.hasHomepage()) {
            homepageButton.setVisibility(View.VISIBLE);
            homepageButton.setOnClickListener(view ->
                    openExternal(person.getHomepage(), person.getName() + "'s Homepage"));
        }

        if (person.hasImdb()) {
            imdbButton.setVisibility(View.VISIBLE);
            imdbButton.setOnClickListener(view ->
                    openExternal(person.getImbdUrl(), person.getName() + "'s IMDB"));
        }
    }

    private void openExternal(String url, String title) {
        WebviewActivity.startInstance(this, url, title);
    }

    void gotError(Throwable e) {
        e.printStackTrace();
    }

    static class ViewPagerAdapter extends PagerAdapter {
        LayoutInflater inflater;
        Person person;

        public ViewPagerAdapter(LayoutInflater inflater, Person person) {
            this.inflater = inflater;
            this.person = person;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = container.getContext();
            View view;
            int padding = (int) context.getResources().getDimension(R.dimen.activity_horizontal_margin);
            if (position == 0) {
                view = inflater.inflate(R.layout.layout_biography, container, false);
                ((TextView) view.findViewById(R.id.biography)).setText(person.getBiography());
            } else if (position == 2) {
                RecyclerView recyclerView = new RecyclerView(context);
                GridLayoutManager layoutManager = new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(new GalleryAdapter(person.getProfileImages()));
                view = recyclerView;
            } else {
                StickyListHeadersListView listView = new StickyListHeadersListView(context);
                listView.setAdapter(new PersonCreditsAdapter(person.getCreditList(), inflater));
                view = listView;
            }
            view.setPadding(padding, padding, padding, padding);
            container.addView(view);
            return view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0)
                return "Biography";
            else if (position == 1)
                return "Credits";
            else
                return "Gallery";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DataBag.removePersonFromPocket(personId);
    }
}
