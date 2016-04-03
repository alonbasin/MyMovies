package com.alma.mymovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Alon on 4/2/2016.
 */
public class GridAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = GridAdapter.class.getSimpleName();
    private Context mContext;

    public GridAdapter(Context context, List<Movie> moviesList) {
        super(context, 0, moviesList);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext)
                .load(movie.mPoster)
                .resize(700, 1000)
                .into(imageView);

        return imageView;
    }

    @Override
    public Movie getItem(int position) {
        return super.getItem(position);
    }
}
