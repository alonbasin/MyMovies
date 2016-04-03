package com.alma.mymovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Alon on 4/2/2016.
 */
public class GridAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = GridAdapter.class.getSimpleName();

    public GridAdapter(Activity context, ArrayList<Movie> moviesList) {
        super(context, 0,moviesList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grid_item, parent, false);
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.movie_poster);
        //image.setImageResource(androidFlavor.image);
        Picasso.with(getContext()).load(movie.mPoster).into(image);

        return convertView;
    }

    @Override
    public Movie getItem(int position) {
        return super.getItem(position);
    }

}
