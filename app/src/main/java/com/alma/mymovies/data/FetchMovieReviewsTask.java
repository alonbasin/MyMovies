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

/**
 * Created by Alon on 4/9/2016.
 */
public class FetchMovieReviewsTask extends AsyncTask<Movie, Void, ArrayList<String>> {

    private final String LOG_TAG = FetchMovieReviewsTask.class.getSimpleName();

    private ArrayAdapter<String> mReviewsAdapter;
    private final Context mContext;
    private LinearLayout reviewsLayout;

    public FetchMovieReviewsTask(Context context, ArrayAdapter<String> reviewsAdapter, View rootView) {
        mReviewsAdapter = reviewsAdapter;
        mContext = context;
        reviewsLayout = (LinearLayout) rootView;
    }

    @Override
    protected ArrayList<String> doInBackground(Movie... params) {

        Movie movie = params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieReviewsJsonStr = null;

        try {
            final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String MOVIE_ID = movie.mId;
            final String REVIEWS_REQUEST = "reviews";
            final String API_KRY_PARAM = "api_key";

            Uri reviewsUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendPath(MOVIE_ID)
                    .appendPath(REVIEWS_REQUEST)
                    .appendQueryParameter(API_KRY_PARAM, BuildConfig.MOVIES_API_KEY)
                    .build();

            URL url = new URL(reviewsUri.toString());
            Log.d("reviewsUrl", url.toString());

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

            movieReviewsJsonStr = buffer.toString();

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
            return getReviewsListFromJson(movieReviewsJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<String> getReviewsListFromJson(String movieReviewsJsonStr) throws JSONException {

        final String API_RESULTS = "results";
        final String MOVIE_REVIEW_CONTENT = "content";

        ArrayList<String> reviewsList = new ArrayList<>();

        JSONObject reviewJson = new JSONObject(movieReviewsJsonStr);
        JSONArray reviewsJsonArrsy = reviewJson.getJSONArray(API_RESULTS);

        for (int i = 0; i < reviewsJsonArrsy.length(); i++) {

            JSONObject reviewJsonObject = reviewsJsonArrsy.getJSONObject(i);
            String content = reviewJsonObject.getString(MOVIE_REVIEW_CONTENT);

            reviewsList.add(content);
        }
        return reviewsList;
    }

    @Override
    protected void onPostExecute(ArrayList<String> reviews) {
        super.onPostExecute(reviews);

        if ((!reviews.isEmpty()) && mReviewsAdapter != null) {
            reviewsLayout.setVisibility(View.VISIBLE);
            mReviewsAdapter.clear();
            mReviewsAdapter.addAll(reviews);
        }
    }
}
