package com.alma.mymovies;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alma.mymovies.sync.FetchMovieReviewsTask;
import com.alma.mymovies.sync.FetchMovieTrailersTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DetailsFragment extends Fragment {

    private Movie mMovie;
    protected TextView mTitleTextView, mReleaseDateTextView, mVotesTextView ,mOverviewTextView;
    protected LinearLayout mReviewsLayout, mTrailersLayout;
    protected ImageView mPosterImageView;
    protected ListView mTrailersListView, mReviewsListView;
    protected ArrayList<Trailer> mTrailersList;
    protected ArrayList<String> mReviewsList;
    protected ArrayAdapter<String> reviewsAdapter;
    protected TrailersAdapter<Trailer> trailersAdapter;

    public DetailsFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Intent intent = getActivity().getIntent();
        mMovie = intent.getParcelableExtra("selected_movie");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        mReviewsLayout = (LinearLayout) view.findViewById(R.id.reviews_layout);
        mTrailersLayout = (LinearLayout) view.findViewById(R.id.trailers_layout);

        mTitleTextView = (TextView) view.findViewById(R.id.titleTextView);
        mTitleTextView.setText(mMovie.mTitle);

        mPosterImageView = (ImageView) view.findViewById(R.id.posterImageView);
        Picasso.with(getActivity())
                .load(mMovie.mPoster)
                .resize(600, 900)
                .into(mPosterImageView);

        mReleaseDateTextView = (TextView) view.findViewById(R.id.releaseDateTextView);
        mReleaseDateTextView.setText(mMovie.mReleaseDate);

        mVotesTextView = (TextView) view.findViewById(R.id.votsTextView);
        mVotesTextView.setText(mMovie.mVoteAverage);

        mOverviewTextView = (TextView) view.findViewById(R.id.overviewTextView);
        mOverviewTextView.setText(mMovie.mOverview);

        //trailers
        mTrailersList = new ArrayList<>();
        trailersAdapter = new TrailersAdapter<>(getActivity(),
                R.layout.trailer_item,
                mTrailersList);

        FetchMovieTrailersTask fetchMovieTrailersTask = new FetchMovieTrailersTask(getActivity(),
                trailersAdapter,
                mTrailersLayout);
        fetchMovieTrailersTask.execute(mMovie);

        mTrailersListView = (ListView) view.findViewById(R.id.trailers_listView);
        mTrailersListView.setAdapter(trailersAdapter);

        mTrailersListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        mTrailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(mTrailersList.get(position).mTrailerUrl)));

            }
        });

        //reviews
        mReviewsList = new ArrayList<>();
        reviewsAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1,
                mReviewsList);

        FetchMovieReviewsTask fetchMovieReviewsTask = new FetchMovieReviewsTask(getActivity(),
                reviewsAdapter,
                mReviewsLayout);
        fetchMovieReviewsTask.execute(mMovie);

        mReviewsListView = (ListView) view.findViewById(R.id.reviews_listView);
        mReviewsListView.setAdapter(reviewsAdapter);

        return view;
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


}
