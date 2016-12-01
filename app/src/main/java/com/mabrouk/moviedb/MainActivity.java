package com.mabrouk.moviedb;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    String[] sections;
    ActionBarDrawerToggle toggle;
    MainSectionsFactory factory = new MainSectionsFactory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menu_open_desc, R.string.menu_close_desc);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        sections = getResources().getStringArray(R.array.drawer_list);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this::selectSection);
        selectSection(navigationView.getMenu().findItem(R.id.movies));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    boolean selectSection(MenuItem item) {
        int position = 0;
        switch (item.getItemId()) {
            case R.id.movies:
                position = 0;
                break;
            case R.id.tv:
                position = 1;
                break;
            case R.id.people:
                position = 2;
                break;
            case R.id.genres:
                position = 3;
                break;
        }
        setTitle(sections[position]);
        drawerLayout.closeDrawers();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, factory.fragmentForPage(position))
                .commit();
        item.setChecked(true);
        return true;
    }
}
