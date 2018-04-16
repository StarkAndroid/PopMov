package com.example.serwis.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by serwis on 2018-04-14.
 */

public class Trailer implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
