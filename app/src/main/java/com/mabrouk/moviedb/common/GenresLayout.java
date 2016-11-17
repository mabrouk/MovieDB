package com.mabrouk.moviedb.common;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mabrouk.moviedb.R;
import com.mabrouk.moviedb.genres.Genre;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;

/**
 * Created by User on 11/12/2016.
 */

public class GenresLayout extends FrameLayout {
    public interface OnGenreClickedListener {
        void onGenreClicked(Genre genre);
    }

    ProgressBar progressBar;
    TextView emptyTextView;
    FlowLayout flowLayout;
    private OnGenreClickedListener listener;

    public GenresLayout(Context context) {
        super(context);
        init(context);
    }

    public GenresLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GenresLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.genres_layout, this, true);
        emptyTextView = (TextView) findViewById(R.id.genres_textview);
        flowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        setLoading();
    }

    public void setOnGenreClickedListener(OnGenreClickedListener listener) {
        this.listener = listener;
    }

    public void setMessage(String message) {
        progressBar.setVisibility(GONE);
        emptyTextView.setText(message);
        emptyTextView.setVisibility(VISIBLE);
    }

    public void setGenres(List<Genre> genres) {
        progressBar.setVisibility(GONE);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        int backgroundResource = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ? R.drawable.shape_rounded_rect
                : R.drawable.genre_ripple_background;

        for (Genre genre : genres) {
            View buttonLayout = inflater.inflate(R.layout.button_gener, null);
            Button button = (Button) buttonLayout.findViewById(R.id.button);
            button.setBackgroundResource(backgroundResource);
            button.setText(genre.getName());
            button.setOnClickListener(btn -> genreClicked(genre));
            flowLayout.addView(buttonLayout);
        }
    }

    private void genreClicked(Genre genre) {
        if (listener != null)
            listener.onGenreClicked(genre);
    }

    public void setLoading() {
        emptyTextView.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
    }
}
