package com.alma.mymovies.data;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.alma.mymovies.BuildConfig;
import com.alma.mymovies.Movie;
import com.alma.mymovies.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FetchMovieTrailersTask extends AsyncTask<Movie, Void, ArrayList<Trailer>> {

    private final String LOG_TAG = FetchMovieTrailersTask.class.getSimpleName();

    private ArrayAdapter<Trailer> mTrailersAdapter;
    private final Context mContext;
    private LinearLayout trailersLayout;

    public FetchMovieTrailersTask(Context context, ArrayAdapter<Trailer> trailerAdapter, View rootView) {
        mTrailersAdapter = trailerAdapter;
        mContext = context;
        trailersLayout = (LinearLayout) rootView;
    }

    @Override
    protected ArrayList<Trailer> doInBackground(Movie... params) {

        Movie movie = params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieTrailersJsonStr = null;

        try {
            final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String MOVIE_ID = movie.mId;
            final String REVIEWS_REQUEST = "videos";
            final String API_KRY_PARAM = "api_key";

            Uri trailersUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendPath(MOVIE_ID)
                    .appendPath(REVIEWS_REQUEST)
                    .appendQueryParameter(API_KRY_PARAM, BuildConfig.MOVIES_API_KEY)
                    .build();

            URL url = new URL(trailersUri.toString());
            Log.d("trailersUrl", url.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            movieTrailersJsonStr = buffer.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getTrailersListFromJson(movieTrailersJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Trailer> getTrailersListFromJson(String movieTrailersJsonStr) throws JSONException {

        final String API_RESULTS = "results";
        final String MOVIE_TRAILER_KEY = "key";
        final String MOVIE_TRAILER_NAME = "name";

        ArrayList<Trailer> trailersList = new ArrayList<>();

        JSONObject trailerJson = new JSONObject(movieTrailersJsonStr);
        JSONArray trailersJsonArray = trailerJson.getJSONArray(API_RESULTS);

        for (int i = 0; i < trailersJsonArray.length(); i++) {

            JSONObject trailerJsonObject = trailersJsonArray.getJSONObject(i);
            String key = trailerJsonObject.getString(MOVIE_TRAILER_KEY);
            String name = trailerJsonObject.getString(MOVIE_TRAILER_NAME);

            Trailer trailer = new Trailer(name, key);
            trailersList.add(trailer);
        }
        return trailersList;
    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> trailers) {
        super.onPostExecute(trailers);

        if ((!trailers.isEmpty()) && mTrailersAdapter != null) {
            trailersLayout.setVisibility(View.VISIBLE);
            mTrailersAdapter.clear();
            mTrailersAdapter.addAll(trailers);
        }
    }
}
