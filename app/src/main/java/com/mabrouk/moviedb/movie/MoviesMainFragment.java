package com.mabrouk.moviedb.movie;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.PagesLoader;
import com.mabrouk.moviedb.movie.api.MovieServiceProvider;
import com.mabrouk.moviedb.movie.factory.MovieListFragmentFactory;

public class MoviesMainFragment extends Fragment implements SearchView.OnQueryTextListener{
    MovieListFragmentFactory factory = new MovieListFragmentFactory();
    static final String SEARCH_TAG = "movie_search";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        ViewPager viewPager = (ViewPager) root.findViewById(R.id. view_pager);
        MoviesPagerAdapter adapter = new MoviesPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tab_bar);
        tabLayout.setupWithViewPager(viewPager);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Fragment fragment = getFragmentManager().findFragmentByTag(SEARCH_TAG);
                if(fragment != null)
                    getFragmentManager().beginTransaction().remove(fragment).commit();
                return true;
            }
        });
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        MovieListFragment fragment = (MovieListFragment) getFragmentManager().findFragmentByTag(SEARCH_TAG);

        if(fragment == null)
            fragment = new MovieListFragment();

        fragment.setPagesLoader(new PagesLoader<Movie>(page -> MovieServiceProvider.getService().search(query, page)));
        getFragmentManager().beginTransaction()
                .replace(R.id.search_frame, fragment, SEARCH_TAG)
                .commit();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    class MoviesPagerAdapter extends FragmentPagerAdapter {

        MoviesPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return factory.fragmentForPage(position);
        }

        @Override
        public int getCount() {
            return MovieListFragmentFactory.SECTIONS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(MovieListFragmentFactory.pageTitleRes(position));
        }
    }
}
