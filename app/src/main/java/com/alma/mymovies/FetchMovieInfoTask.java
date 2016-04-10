package com.alma.mymovies;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.net.HttpURLConnection;

/**
 * Created by Alon on 4/9/2016.
 */
public class FetchMovieInfoTask extends AsyncTask<Movie, Void, Void> {
    private final String LOG_TAG = FetchMovieInfoTask.class.getSimpleName();

    @Override
    protected Void doInBackground(Movie... params) {

        Movie movie = params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieInfoStr[] = null;


        return null;
    }
}
