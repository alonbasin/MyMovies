package com.alma.mymovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alon on 4/2/2016.
 */
public class Movie implements Parcelable {

    public String mTitle, mPoster, mOverview, mReleaseDate, mVoteAverage;

    private final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private StringBuilder sb = new StringBuilder(POSTER_BASE_URL);

    public enum PosterWidth {
        WIDTH185 ("w185"),
        WIDTH500 ("w500");

        private final String posterWidth;

        private PosterWidth(String s) {
            posterWidth = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName == null) ? false : posterWidth.equals(otherName);
        }

        public String toString() {
            return posterWidth;
        }
    }

    public Movie(String title, String poster, String overview, String releaseDate, String voteAverage) {
        mTitle = title;
        mPoster = sb.append(PosterWidth.WIDTH185).append(poster).toString();
        mOverview = overview;
        mReleaseDate = releaseDate;
        mVoteAverage = voteAverage;
    }

    protected Movie(Parcel in) {
        mTitle = in.readString();
        mPoster = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mVoteAverage = in.readString();
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
        dest.writeString(mVoteAverage);
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
