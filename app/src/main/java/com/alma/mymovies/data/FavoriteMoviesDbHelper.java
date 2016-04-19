package com.alma.mymovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alma.mymovies.Movie;

import java.util.ArrayList;

/**
 * Created by Alon on 4/18/2016.
 */
public class FavoriteMoviesDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "favorite_movies.db";


    public FavoriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_MOVIES_TABLE = "CREATE TABLE " + FavoriteMoviesContract.TABLE_NAME + " (" +
                FavoriteMoviesContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                FavoriteMoviesContract.COLUMN_ID + " TEXT NOT NULL, " +
                FavoriteMoviesContract.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteMoviesContract.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavoriteMoviesContract.COLUMN_POSTER_URL + " TEXT NOT NULL, " +
                FavoriteMoviesContract.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                FavoriteMoviesContract.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL " +
                " );";

        db.execSQL(SQL_CREATE_FAVORITE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.TABLE_NAME);
        onCreate(db);
    }

    private boolean insertFavoriteMovie(String id, String title, String overview, String poster, String releaseDate, String voteAverage) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues favoriteMovieValues = new ContentValues();

        favoriteMovieValues.put(FavoriteMoviesContract.COLUMN_ID, id);
        favoriteMovieValues.put(FavoriteMoviesContract.COLUMN_TITLE, title);
        favoriteMovieValues.put(FavoriteMoviesContract.COLUMN_OVERVIEW, overview);
        favoriteMovieValues.put(FavoriteMoviesContract.COLUMN_POSTER_URL, poster);
        favoriteMovieValues.put(FavoriteMoviesContract.COLUMN_RELEASE_DATE, releaseDate);
        favoriteMovieValues.put(FavoriteMoviesContract.COLUMN_VOTE_AVERAGE, voteAverage);

        db.insert(FavoriteMoviesContract.TABLE_NAME, null, favoriteMovieValues);
        db.close();

        return true;
    }

    public Cursor getData(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * from " + FavoriteMoviesContract.TABLE_NAME + " WHERE " + FavoriteMoviesContract.COLUMN_ID + "=" + id, null);
        db.close();

        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, FavoriteMoviesContract.TABLE_NAME);
        db.close();

        return numRows;
    }

    public boolean deleteContact (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FavoriteMoviesContract.TABLE_NAME, FavoriteMoviesContract.COLUMN_ID + "=" + id, null);
        db.close();

        return true;
    }

    public ArrayList<Movie> getAllFavoriteMovies() {
        ArrayList<Movie> movies = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * from " + FavoriteMoviesContract.TABLE_NAME, null);

        while(res.moveToNext()){
            String id, title, overview, poster, releaseDate, voteAverage;

            id = res.getString(res.getColumnIndex(FavoriteMoviesContract.COLUMN_ID));
            title = res.getString(res.getColumnIndex(FavoriteMoviesContract.COLUMN_TITLE));
            overview = res.getString(res.getColumnIndex(FavoriteMoviesContract.COLUMN_OVERVIEW));
            poster = res.getString(res.getColumnIndex(FavoriteMoviesContract.COLUMN_POSTER_URL));
            releaseDate = res.getString(res.getColumnIndex(FavoriteMoviesContract.COLUMN_RELEASE_DATE));
            voteAverage = res.getString(res.getColumnIndex(FavoriteMoviesContract.COLUMN_VOTE_AVERAGE));

            Movie movie = new Movie(id, title, overview, poster, releaseDate, voteAverage);
            movies.add(movie);
        }

        res.close();
        db.close();

        return movies;
    }
}
