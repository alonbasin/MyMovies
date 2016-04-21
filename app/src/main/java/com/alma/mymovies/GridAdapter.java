package com.alma.mymovies;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
    private DisplayMetrics displaymetrics;
    private WindowManager windowManager;
    private int height;
    private int width;

    public GridAdapter(Context context, List<Movie> moviesList) {
        super(context, 0, moviesList);
        mContext = context;
        getScreenMeasures();
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
                .load(movie.getPosterUrl())
                .resize(width / 2, height / 2)
                .into(imageView);

        return imageView;
    }

    @Override
    public Movie getItem(int position) {
        return super.getItem(position);
    }

    public void getScreenMeasures() {
        displaymetrics = new DisplayMetrics();
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
    }

}
