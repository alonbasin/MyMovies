package com.alma.mymovies.sync;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.alma.mymovies.BuildConfig;
import com.alma.mymovies.Movie;
import com.alma.mymovies.data.FavoriteMoviesDbHelper;

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
public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    private ArrayAdapter<Movie> mMovieAdapter;
    private final Context mContext;

    public FetchMoviesTask(Context context, ArrayAdapter<Movie> moviesAdapter) {
        mMovieAdapter = moviesAdapter;
        mContext = context;
    }


    @Override
    protected ArrayList<Movie> doInBackground(String... params) {

        String requestType = params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonStr = null;

        if (requestType.equals("favorites")) {
            FavoriteMoviesDbHelper dbHelper = new FavoriteMoviesDbHelper(mContext);
            ArrayList<Movie> movies = dbHelper.getAllFavoriteMovies();
            Log.d("favoriteMoviesList", String.valueOf(movies));
            return movies;
        } else {
            try {
                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String REQUEST_TYPE = requestType;
                final String API_KRY_PARAM = "api_key";

                Uri moviesUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendPath(REQUEST_TYPE)
                        .appendQueryParameter(API_KRY_PARAM, BuildConfig.MOVIES_API_KEY)
                        .build();

                URL url = new URL(moviesUri.toString());
                Log.d("url", url.toString());

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

                moviesJsonStr = buffer.toString();

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
                return getMoviesListFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

    }

    private ArrayList<Movie> getMoviesListFromJson(String moviesJsonStr) throws JSONException{

        final String API_RESULTS = "results";
        final String MOVIE_ID = "id";
        final String MOVIE_TITLE = "title";
        final String MOVIE_POSTER = "poster_path";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_VOTE_AVERAGE = "vote_average";

        ArrayList<Movie> moviesList = new ArrayList<>();

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray moviesJsonArray = moviesJson.getJSONArray(API_RESULTS);

        for (int i = 0; i < moviesJsonArray.length(); i++) {

            JSONObject movieJsonObject = moviesJsonArray.getJSONObject(i);
            String id = movieJsonObject.getString(MOVIE_ID);
            String title = movieJsonObject.getString(MOVIE_TITLE);
            String poster = movieJsonObject.getString(MOVIE_POSTER);
            String overview = movieJsonObject.getString(MOVIE_OVERVIEW);
            String releaseDate = movieJsonObject.getString(MOVIE_RELEASE_DATE);
            String voteAverage = movieJsonObject.getString(MOVIE_VOTE_AVERAGE);

            Movie movie = new Movie(id, title, poster, overview, releaseDate, voteAverage);
            moviesList.add(movie);
        }

        for (Movie movie:moviesList) {
            Log.d("moviess", movie.toString());
        }

        Log.d("moviess", String.valueOf(moviesList.size()));
        return moviesList;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        super.onPostExecute(movies);

        if ((!movies.isEmpty()) && mMovieAdapter != null) {
            mMovieAdapter.clear();
            mMovieAdapter.addAll(movies);
        }
    }

}
