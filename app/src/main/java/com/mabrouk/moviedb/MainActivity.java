package com.mabrouk.moviedb;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    String[] sections;

    MainSectionsFactory factory = new MainSectionsFactory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        sections = getResources().getStringArray(R.array.drawer_list);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this::selectSection);
        selectSection(navigationView.getMenu().findItem(R.id.movies));
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
