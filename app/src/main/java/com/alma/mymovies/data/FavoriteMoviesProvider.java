//package com.alma.mymovies.data;
//
//import android.annotation.TargetApi;
//import android.content.ContentProvider;
//import android.content.ContentValues;
//import android.content.UriMatcher;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.net.Uri;
//import android.support.annotation.Nullable;
//
///**
// * Created by Alon on 4/19/2016.
// */
//public class FavoriteMoviesProvider extends ContentProvider {
//
//    private static final UriMatcher sUriMatcher = buildUriMatcher();
//    private FavoriteMoviesDbHelper mOpenHelper;
//
//    static final int FAVORITES = 100;
//
//
//    static UriMatcher buildUriMatcher() {
//
//        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
//        final String authority = FavoriteMoviesContract.CONTENT_AUTHORITY;
//
//        matcher.addURI(authority, FavoriteMoviesContract.PATH_FAVORITE_MOVIES, FAVORITES);
//
//        return matcher;
//    }
//
//    @Override
//    public boolean onCreate() {
//        mOpenHelper = new FavoriteMoviesDbHelper(getContext());
//        return true;
//    }
//
//    @Nullable
//    @Override
//    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
//        return null;
//    }
//
//    @Nullable
//    @Override
//    public String getType(Uri uri) {
//
//        final int match = sUriMatcher.match(uri);
//
//        switch (match) {
//
//            case FAVORITES:
//                return FavoriteMoviesContract.CONTENT_TYPE;
//
//            default:
//                throw new UnsupportedOperationException("Unknown uri: " + uri);
//        }
//    }
//
//    @Nullable
//    @Override
//    public Uri insert(Uri uri, ContentValues values) {
//        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
//        final int match = sUriMatcher.match(uri);
//        Uri returnUri;
//
//        switch (match) {
//            case FAVORITES: {
//                long _id = db.insert(FavoriteMoviesContract.TABLE_NAME, null, values);
//                if ( _id > 0 )
//                    returnUri = FavoriteMoviesContract.buildFavoriteMoviesUri(_id);
//                else
//                    throw new android.database.SQLException("Failed to insert row into " + uri);
//                break;
//            }
//            default:
//                throw new UnsupportedOperationException("Unknown uri: " + uri);
//        }
//        getContext().getContentResolver().notifyChange(uri, null);
//        return returnUri;
//    }
//
//    @Override
//    public int delete(Uri uri, String selection, String[] selectionArgs) {
//        return 0;
//    }
//
//    @Override
//    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
//        return 0;
//    }
//
//    @Override
//    @TargetApi(11)
//    public void shutdown() {
//        mOpenHelper.close();
//        super.shutdown();
//    }
//}
