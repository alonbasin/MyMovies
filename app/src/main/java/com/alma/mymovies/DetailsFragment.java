package com.alma.mymovies;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alma.mymovies.data.FavoriteMoviesDbHelper;
import com.alma.mymovies.sync.FetchMovieReviewsTask;
import com.alma.mymovies.sync.FetchMovieTrailersTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DetailsFragment extends Fragment {

    static final String SELECTED_MOVIE = "selected_movie";

    private Movie mMovie;
    protected TextView mTitleTextView, mReleaseDateTextView, mVotesTextView ,mOverviewTextView;
    protected Button mFavoriteButton;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mMovie = args.getParcelable(DetailsFragment.SELECTED_MOVIE);
        } else {
            Log.d("snap", "snap");
        }

        View view = inflater.inflate(R.layout.fragment_details, container, false);

        mReviewsLayout = (LinearLayout) view.findViewById(R.id.reviews_layout);
        mTrailersLayout = (LinearLayout) view.findViewById(R.id.trailers_layout);

        mTitleTextView = (TextView) view.findViewById(R.id.titleTextView);
        mTitleTextView.setText(mMovie.mTitle);

        mPosterImageView = (ImageView) view.findViewById(R.id.posterImageView);
        Picasso.with(getActivity())
                .load(mMovie.getPosterUrl())
                .resize(600, 900)
                .into(mPosterImageView);

        mReleaseDateTextView = (TextView) view.findViewById(R.id.releaseDateTextView);
        mReleaseDateTextView.setText(mMovie.mReleaseDate);

        mVotesTextView = (TextView) view.findViewById(R.id.votsTextView);
        mVotesTextView.setText(mMovie.mVoteAverage);

        mFavoriteButton = (Button) view.findViewById(R.id.favorite_button);
        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteMoviesDbHelper dbHelper = new FavoriteMoviesDbHelper(getActivity());
                if (dbHelper.isFavorite(mMovie.mId)) {
                    dbHelper.deleteFavoriteMovie(mMovie.mId);
                    Toast.makeText(getActivity(), "Deleted from favorites!", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.insertFavoriteMovie(mMovie.mId, mMovie.mTitle, mMovie.mOverview, mMovie.mPoster, mMovie.mReleaseDate, mMovie.mVoteAverage);
                    Toast.makeText(getActivity(), "Added to favorites!", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
        inflater.inflate(R.menu.fragment_details_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        } else if (id == R.id.action_share) {
            if (mTrailersList.size() > 0) {
                String trailer = mTrailersList.get(0).mTrailerUrl;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this trailer: " + trailer);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            } else {
                Toast.makeText(getActivity(), "There's no trailers!", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
