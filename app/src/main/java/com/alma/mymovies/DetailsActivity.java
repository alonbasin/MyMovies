package com.alma.mymovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailsFragment.SELECTED_MOVIE, getIntent().getParcelableExtra(DetailsFragment.SELECTED_MOVIE));

            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_container, fragment)
                    .commit();
        }
    }
}
