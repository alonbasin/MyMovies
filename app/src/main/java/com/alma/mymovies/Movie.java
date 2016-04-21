package com.alma.mymovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alon on 4/2/2016.
 */
public class Movie implements Parcelable {

    private final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private StringBuilder sb = new StringBuilder(POSTER_BASE_URL);

    public String mId, mTitle, mPoster, mOverview, mReleaseDate, mVoteAverage;

    public Movie(String id, String title, String poster, String overview, String releaseDate, String voteAverage) {
        mId = id;
        mTitle = title;
        mPoster = poster;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mVoteAverage = voteAverage;
    }

    public String getPosterUrl() {
        return sb.append(PosterWidth.WIDTH185).append(mPoster).toString();
    }

    protected Movie(Parcel in) {
        mId = in.readString();
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
        dest.writeString(mId);
        dest.writeString(mTitle);
        dest.writeString(mPoster);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeString(mVoteAverage);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "mId='" + mId + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mPoster='" + mPoster + '\'' +
                ", mOverview='" + mOverview + '\'' +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mVoteAverage='" + mVoteAverage + '\'' +
                '}';
    }

    public enum PosterWidth {
        WIDTH185 ("w185"),
        WIDTH500 ("w500");

        private final String posterWidth;

        private PosterWidth(String s) {
            posterWidth = s;
        }

        public String toString() {
            return posterWidth;
        }
    }
}
