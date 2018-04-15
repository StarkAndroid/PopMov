package com.example.serwis.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by serwis on 2018-04-01.
 */

public class MovieContract {

    public static final String CONETNET_AUTHORITY = "com.example.serwis.popularmovies";
    private static final Uri BASE_URI = Uri.parse("content://" + CONETNET_AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME = "FAVOURITES_MOVIES";
        public static final String MOVIE_ID = "MOVIEid";
        public static final String MOVIE_TITLE = "MOVIEtitle";
    }

}
