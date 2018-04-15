package com.example.serwis.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by serwis on 2018-04-01.
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    private final static int version = 2;
    private static final String DATABASE_NAME = "favouritesMovies.db";


    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null,  version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +

            MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieContract.MovieEntry.MOVIE_ID + " TEXT, " +
            MovieContract.MovieEntry.MOVIE_TITLE + " TEXT);";

    db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
