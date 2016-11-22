package com.mabrouk.moviedb.people;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.PagesLoader;

/**
 * Created by VPN on 11/22/2016.
 */

public class PeopleMainFragment extends Fragment implements SearchView.OnQueryTextListener{
    static final String SEARCH_TAG = "people_search";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_people_main, container, false);
        PeopleListFragment popularPeople = new PeopleListFragment();
        popularPeople.setPagesLoader(new PagesLoader<>(PeopleServiceProvider.getService()::getPopularPeople));
        getFragmentManager().beginTransaction()
                .add(R.id.root_layout, popularPeople)
                .commit();
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
        PeopleListFragment fragment = (PeopleListFragment) getFragmentManager().findFragmentByTag(SEARCH_TAG);

        boolean add = false;
        if(fragment == null) {
            fragment = new PeopleListFragment();
            add = true;
        }

        fragment.setPagesLoader(new PagesLoader<Person>(page -> PeopleServiceProvider.getService().search(query, page)));

        if (add) {
            getFragmentManager().beginTransaction()
                    .add(R.id.root_layout, fragment, SEARCH_TAG)
                    .commit();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
