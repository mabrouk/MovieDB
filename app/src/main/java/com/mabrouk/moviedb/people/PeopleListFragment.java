package com.mabrouk.moviedb.people;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.common.ListFragment;
import com.mabrouk.moviedb.common.PagesLoader;
import com.mabrouk.moviedb.common.PagingAdapter;

import java.util.List;

public class PeopleListFragment extends ListFragment<Person> {

    @Override
    protected PagingAdapter<Person, ?> initAdapter(Context context) {
        return new PeopleListAdapter(context);
    }
}
