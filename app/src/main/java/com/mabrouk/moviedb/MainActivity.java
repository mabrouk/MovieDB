package com.mabrouk.moviedb;

import android.annotation.SuppressLint;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ListView drawerList;
    String[] sections;

    MainSectionsFactory factory = new MainSectionsFactory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        sections = getResources().getStringArray(R.array.drawer_list);

        drawerList = (ListView) findViewById(R.id.drawer_list);
        drawerList.setAdapter(new DrawerListAdapter(sections));
        drawerList.setOnItemClickListener((parent, view, position, id) -> selectSection(position));

        selectSection(0);
    }

    private void selectSection(int position) {
        setTitle(sections[position]);
        drawerLayout.closeDrawer(drawerList);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, factory.fragmentForPage(position))
                .commit();
    }

    static class DrawerListAdapter extends BaseAdapter {
        String[] sections;
        public DrawerListAdapter(String[] sections) {
            this.sections = sections;
        }

        @Override
        public int getCount() {
            return sections.length;
        }

        @Override
        public Object getItem(int position) {
            return sections[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.row_drawer_list, parent, false);
            ((TextView) convertView.findViewById(R.id.section)).setText(sections[position]);
            return convertView;
        }
    }
}
