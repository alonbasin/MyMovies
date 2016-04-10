package com.alma.mymovies;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;


public class MainFragment extends Fragment {

    private static final String POPULAR_REQUEST = "popular";
    private static final String TOP_RATED_REQUEST = "top_rated";

    private GridAdapter mGridAdapter;
    private ArrayList<Movie> mMovieList;

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieList = new ArrayList<>();

        if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            refreshMovies();
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
        setHasOptionsMenu(true);

        mGridAdapter = new GridAdapter(getActivity(), mMovieList);
        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        gridView.setAdapter(mGridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FetchMovieInfoTask fetchMovieInfoTask = new FetchMovieInfoTask();
                fetchMovieInfoTask.execute(mMovieList.get(position));

                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("selected_movie", mMovieList.get(position));
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshMovies();
    }

    private void refreshMovies() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String requestType = preferences.getString("requestType", POPULAR_REQUEST);
        FetchMoviesTask moviesTask = new FetchMoviesTask(this.getActivity(), mGridAdapter);
        moviesTask.execute(requestType);
    }

}



