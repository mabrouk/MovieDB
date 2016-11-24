package com.mabrouk.moviedb.people.details;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.movie.details.MovieDetailsActivity;
import com.mabrouk.moviedb.tv.details.TvDetailsActivity;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by User on 11/9/2016.
 */

public class PersonCreditsAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    LayoutInflater inflater;
    List<PersonCredit> creditsList;

    public PersonCreditsAdapter(List<PersonCredit> creditsList, LayoutInflater inflater) {
        this.inflater = inflater;
        this.creditsList = creditsList;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = inflater.inflate(R.layout.header_person_credit, parent, false);
        TextView header = (TextView) convertView.findViewById(R.id.header);
        PersonCredit credit = creditsList.get(position);
        if (credit instanceof PersonAsCast) {
            header.setText(credit.isMovieCredit() ? "Actor/Movies" : "Actor/TV ");
        }else{
            PersonAsCrew crewCredit = (PersonAsCrew) credit;
            header.setText(crewCredit.getDepartment());
        }
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        PersonCredit credit = creditsList.get(position);
        if (credit instanceof PersonAsCast) {
            return credit.getMediaType().hashCode();
        }else{
            PersonAsCrew crewCredit = (PersonAsCrew) credit;
            return crewCredit.getDepartment().hashCode();
        }
    }

    @Override
    public int getCount() {
        return creditsList.size();
    }

    @Override
    public Object getItem(int position) {
        return creditsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return creditsList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final PersonCredit credit = creditsList.get(position);
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.row_person_credit, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setOnClickListener(view -> viewClicked(view, credit));
        holder.title.setText(credit.getTitle());
        holder.role.setText(credit.getRole());
        holder.type.setText(credit.getMediaType().toUpperCase());
        return convertView;
    }

    private void viewClicked(View view, PersonCredit credit) {
        if(credit.isMovieCredit()) {
            MovieDetailsActivity.startMovieDetailsActivity(view.getContext(), credit.getId(), credit.getTitle());
        }else{
            TvDetailsActivity.startInstance(credit.getId(), credit.getTitle(), (Activity) view.getContext());
        }
    }

    static class ViewHolder {
        TextView title;
        TextView role;
        TextView type;
        public ViewHolder (View root) {
            title = (TextView) root.findViewById(R.id.title);
            type = (TextView) root.findViewById(R.id.media_type);
            role = (TextView) root.findViewById(R.id.role);
        }
    }
}
