package com.alma.mymovies;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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


public class MainFragment extends Fragment {

    private GridView mGridView;
    private GridAdapter mGridAdapter;
    private ArrayList<Movie> mMovieList;

    public MainFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            FetchDataTask dataTask = new FetchDataTask();
            dataTask.execute();
            //dummy
//            mMovieList = new ArrayList<>();
//            for (int i = 0; i < 5; i++) {
//                Movie movie = new Movie("title"
//                        , "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"
//                        , "overview"
//                        , "release"
//                        , 15.5);
//                mMovieList.add(movie);
//                Log.d("my movie",movie.toString());
//            }
        } else {
            mMovieList = savedInstanceState.getParcelableArrayList("movies");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", mMovieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (GridView) rootView.findViewById(R.id.movies_grid);

        return rootView;
    }

    private class FetchDataTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

        private final String LOG_TAG = FetchDataTask.class.getSimpleName();

        private ArrayList<Movie> mMovieList = new ArrayList<>();

        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {

//            if (params.length == 0) {
//                return null;
//            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonStr = null;

            try {
                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String POPULAR_REQUEST = "popular";
                final String API_KRY_PARAM = "api_key";

                Uri popularMoviesUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendPath(POPULAR_REQUEST)
                        .appendQueryParameter(API_KRY_PARAM, BuildConfig.MOVIES_API_KEY)
                        .build();

                URL url = new URL(popularMoviesUri.toString());

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

        private ArrayList<Movie> getMoviesListFromJson(String moviesJsonStr) throws JSONException{

            final String API_RESULTS = "results";
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
                String title = movieJsonObject.getString(MOVIE_TITLE);
                String poster = movieJsonObject.getString(MOVIE_POSTER);
                String overview = movieJsonObject.getString(MOVIE_OVERVIEW);
                String releaseDate = movieJsonObject.getString(MOVIE_RELEASE_DATE);
                String voteAverage = movieJsonObject.getString(MOVIE_VOTE_AVERAGE);

                Movie movie = new Movie(title, poster, overview, releaseDate, voteAverage);
                moviesList.add(movie);
            }
            return moviesList;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);

            mGridAdapter = new GridAdapter(getActivity(), mMovieList);
            mGridView.setAdapter(mGridAdapter);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        }
    }

}
