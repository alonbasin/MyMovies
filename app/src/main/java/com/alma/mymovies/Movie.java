package com.alma.mymovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alon on 4/2/2016.
 */
public class Movie implements Parcelable {

    public String mTitle, mPoster, mOverview, mReleaseDate;
    public double mVoteAverage;

    public Movie(String title, String poster, String overview, String releaseDate, double voteAverage) {
        mTitle = title;
        mPoster = poster;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mVoteAverage = voteAverage;
    }

    protected Movie(Parcel in) {
        mTitle = in.readString();
        mPoster = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mVoteAverage = in.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mPoster);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeDouble(mVoteAverage);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "mTitle='" + mTitle + '\'' +
                ", mPoster='" + mPoster + '\'' +
                ", mOverview='" + mOverview + '\'' +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mVoteAverage=" + mVoteAverage +
                '}';
    }
}
