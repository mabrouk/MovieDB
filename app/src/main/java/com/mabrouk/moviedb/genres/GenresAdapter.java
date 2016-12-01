package com.mabrouk.moviedb.genres;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mabrouk.moviedb.R;

import java.util.List;

/**
 * Created by VPN on 12/1/2016.
 */

public class GenresAdapter extends BaseAdapter {
    List<Genre> genres;
    public GenresAdapter(List<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public int getCount() {
        return genres.size();
    }

    @Override
    public Object getItem(int position) {
        return genres.get(position);
    }

    @Override
    public long getItemId(int position) {
        return genres.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        Resources resources = context.getResources();
        ViewHolder viewHolder;
        if(convertView == null) {
            TextView textView = new TextView(context);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textView.setTextColor(resources.getColor(R.color.text_genre));
            textView.setPadding((int) resources.getDimension(R.dimen.activity_horizontal_margin),
                    (int) resources.getDimension(R.dimen.basic_margin),
                    (int) resources.getDimension(R.dimen.activity_horizontal_margin),
                    (int) resources.getDimension(R.dimen.basic_margin));

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                textView.setBackground(resources.getDrawable(R.drawable.genre_ripple_background));
            else
                textView.setBackgroundColor(0xffffffff);
            
            viewHolder = new ViewHolder();
            viewHolder.item = textView;
            convertView = textView;
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Genre genre = genres.get(position);
        viewHolder.item.setText(genre.getName());
        viewHolder.item.setOnClickListener(view ->
                MovieByGenreActivity.startInstance((Activity) view.getContext(), genre));
        return viewHolder.item;
    }

    static class ViewHolder {
        TextView item;
    }
}
