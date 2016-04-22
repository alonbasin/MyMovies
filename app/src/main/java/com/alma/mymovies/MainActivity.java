package com.alma.mymovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements MainFragment.Callback{

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.details_container) != null) {
            mTwoPane = true;

//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.details_container, new DetailsFragment())
//                    .commit();
        } else {
            mTwoPane =false;
        }
    }

    @Override
    public void onItemSelected(Movie movie) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(DetailsFragment.SELECTED_MOVIE, movie);

            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setArguments(args);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.details_container, detailsFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(DetailsFragment.SELECTED_MOVIE, movie);
            startActivity(intent);
        }
    }
}
