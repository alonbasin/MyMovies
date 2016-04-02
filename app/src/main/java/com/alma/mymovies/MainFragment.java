package com.alma.mymovies;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
//            FetchDataTask dataTask = new FetchDataTask();
//            dataTask.execute();
            //dummy
            mMovieList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Movie movie = new Movie("title"
                        , "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"
                        , "overview"
                        , "release"
                        , 15.5);
                mMovieList.add(movie);
                Log.d("my movie",movie.toString());
            }
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

        mGridAdapter = new GridAdapter(getActivity(), mMovieList);
        mGridView = (GridView) rootView.findViewById(R.id.movies_grid);
        mGridView.setAdapter(mGridAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        return rootView;
    }
}
