package com.mabrouk.moviedb.people;

import com.mabrouk.moviedb.common.PagesLoader;

import java.util.List;

/**
 * Created by VPN on 11/2/2016.
 */

public class PeoplePagesLoader extends PagesLoader<Person, PersonList>{

    public PeoplePagesLoader(PageLoadingOperation<PersonList> operation) {
        super(operation);
    }

    @Override
    protected List<Person> map(PersonList dataList) {
        return dataList.getPeople();
    }
}
