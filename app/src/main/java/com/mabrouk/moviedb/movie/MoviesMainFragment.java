package com.mabrouk.moviedb.movie;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.movie.factory.MovieListFragmentFactory;

public class MoviesMainFragment extends Fragment {
    MovieListFragmentFactory factory = new MovieListFragmentFactory();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movies_main, container, false);
        ViewPager viewPager = (ViewPager) root.findViewById(R.id. view_pager);
        MoviesPagerAdapter adapter = new MoviesPagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tab_bar);
        tabLayout.setupWithViewPager(viewPager);
        return root;
    }

    class MoviesPagerAdapter extends FragmentPagerAdapter {

        public MoviesPagerAdapter(FragmentManager fm) {
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
