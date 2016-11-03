package com.mabrouk.moviedb.tv;

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

/**
 * Created by VPN on 11/3/2016.
 */

public class TvMainFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        ViewPager viewPager = (ViewPager) root.findViewById(R.id. view_pager);
        TvPagerAdapter adapter = new TvPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tab_bar);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
        return root;
    }

    class TvPagerAdapter extends FragmentPagerAdapter {
        TvListFragmentFactory factory = new TvListFragmentFactory();
        public TvPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return factory.fragmentForPage(position);
        }

        @Override
        public int getCount() {
            return TvListFragmentFactory.SECTIONS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getString(factory.titleForPage(position));
        }
    }
}
