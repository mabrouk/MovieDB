package com.mabrouk.moviedb.people.details;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.DataBag;
import com.mabrouk.moviedb.people.Person;
import com.mabrouk.moviedb.people.ServiceProvider;
import com.squareup.picasso.Picasso;

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
        Picasso.with(this).load(person.getThumbnail()).into(profilePic);

        ServiceProvider.getService().getPersonDetails(personId)
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
        viewPager.setAdapter(new ViewPagerAdapter(getLayoutInflater(), person));
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
            if(position == 0) {
                view = inflater.inflate(R.layout.layout_biography, container, false);
                ((TextView) view.findViewById(R.id.biography)).setText(person.getBiography());
            } else if(position == 2) {
                RecyclerView recyclerView = new RecyclerView(context);
                GridLayoutManager layoutManager = new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(new GalleryAdapter(person.getProfileImages()));
                view = recyclerView;
            }else{
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
            if(position == 0)
                return "Biography";
            else if(position == 1)
                return "Credits";
            else
                return "Gallery";
        }
    }

}
