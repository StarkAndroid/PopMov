package com.example.serwis.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by serwis on 2018-04-02.
 */

public class MovieProvider extends ContentProvider {

    private static final int ALL_MOVIES = 100;
    private static final int SINGLE_MOVIE = 101;

    private static UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDBHelper dbHelper;

    public static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONETNET_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, ALL_MOVIES);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", SINGLE_MOVIE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)){
            case SINGLE_MOVIE: {
                cursor = dbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.MOVIE_ID + " = ? ",
                        selectionArgs,
                        null,
                        null,
                        null
                );
                break;
            }
            case ALL_MOVIES: {
                cursor = dbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        null
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // not implementing this in this app for now
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        Uri returnUri = null;

        switch (sUriMatcher.match(uri)) {
            case ALL_MOVIES:{
                sqLiteDatabase.beginTransaction();
                int rowsInserted = 0;

                try {
                    long _id = sqLiteDatabase.insert(MovieContract.MovieEntry.TABLE_NAME,
                            null, values);
                    if (_id > 0) {
                        returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, _id);
                        rowsInserted++;
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } finally {
                    sqLiteDatabase.endTransaction();
                }
                if (rowsInserted != -1) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
            }
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted=0;

        if (selection==null) selection = "1";

        switch (sUriMatcher.match(uri)) {
            case ALL_MOVIES:{
                numRowsDeleted = dbHelper.getWritableDatabase().delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }
            case SINGLE_MOVIE:{
                numRowsDeleted=dbHelper.getWritableDatabase().delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.MovieEntry.MOVIE_ID + " = ? ",
                        selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri:"+uri);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
