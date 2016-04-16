package com.alma.mymovies;

import android.net.Uri;

/**
 * Created by Alon on 4/16/2016.
 */
public class Trailer {

    public String mTrailerName, mTrailerUrl;

    private final String TRAILER_BASE_URL = "https://www.youtube.com/watch"; //?v=nIGtF3J5kn8
    private final String TRAILER_KEY = "v";


    public Trailer(String name, String key) {
        super();
        Uri trailerUri = Uri.parse(TRAILER_BASE_URL).buildUpon()
                .appendQueryParameter(TRAILER_KEY, key)
                .build();

        mTrailerUrl = trailerUri.toString();
        mTrailerName = name;
    }
}
