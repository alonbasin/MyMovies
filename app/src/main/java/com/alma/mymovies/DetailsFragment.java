package com.alma.mymovies;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DetailsFragment extends Fragment {

    private Movie mMovie;
    private TextView mTitleTextView, mReleaseDateTextView, mVotesTextView ,mOverviewTextView;
    private ImageView mPosterImageView;

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
