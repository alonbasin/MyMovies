package com.alma.mymovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alma.mymovies.Movie;

import java.util.ArrayList;

import static com.alma.mymovies.data.FavoriteMoviesContract.*;

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
        final String SQL_CREATE_FAVORITE_MOVIES_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " TEXT NOT NULL, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_POSTER + " TEXT NOT NULL, " +
                COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                COLUMN_VOTE_AVERAGE + " TEXT NOT NULL " +
                " );";

        db.execSQL(SQL_CREATE_FAVORITE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertFavoriteMovie(String id, String title, String overview, String poster, String releaseDate, String voteAverage) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues favoriteMovieValues = new ContentValues();

        favoriteMovieValues.put(COLUMN_ID, id);
        favoriteMovieValues.put(COLUMN_TITLE, title);
        favoriteMovieValues.put(COLUMN_POSTER, poster);
        favoriteMovieValues.put(COLUMN_OVERVIEW, overview);
        favoriteMovieValues.put(COLUMN_RELEASE_DATE, releaseDate);
        favoriteMovieValues.put(COLUMN_VOTE_AVERAGE, voteAverage);

        db.insert(TABLE_NAME, null, favoriteMovieValues);
        db.close();

        return true;
    }

    public Cursor getData(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + "=" + id, null);
        db.close();

        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();

        return numRows;
    }

    public boolean deleteFavoriteMovie (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + "=" + id, null);
        db.close();

        return true;
    }

    public ArrayList<Movie> getAllFavoriteMovies() {
        ArrayList<Movie> movies = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        while(res.moveToNext()){

            String id = res.getString(res.getColumnIndex(COLUMN_ID));
            String title = res.getString(res.getColumnIndex(COLUMN_TITLE));
            String poster = res.getString(res.getColumnIndex(COLUMN_POSTER));
            String overview = res.getString(res.getColumnIndex(COLUMN_OVERVIEW));
            String releaseDate = res.getString(res.getColumnIndex(COLUMN_RELEASE_DATE));
            String voteAverage = res.getString(res.getColumnIndex(COLUMN_VOTE_AVERAGE));

            Movie movie = new Movie(id, title, poster, overview, releaseDate, voteAverage);
            movies.add(movie);
        }

        res.close();
        db.close();

        return movies;
    }

    public boolean isFavorite(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + id, null);
//        c.close();
//        db.close();

        if (c.getCount() > 0)
            return true;
        else return false;
    }
}
