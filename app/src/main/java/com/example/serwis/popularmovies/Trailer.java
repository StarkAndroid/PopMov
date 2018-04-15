package com.example.serwis.popularmovies;

/**
 * Created by serwis on 2018-04-14.
 */

public class Trailer {

    private String mTitle;
    private String mKey;

    public Trailer (String Key, String Title){
        mTitle = Title;
        mKey = Key;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmKey() {
        return mKey;
    }
}
