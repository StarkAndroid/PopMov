package com.example.serwis.popularmovies;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by serwis on 2018-03-06.
 */

public class Movie implements Serializable {
    private String mID;
    private String mTitle;
    private String mOverview;
    private String mRating;
    private String mReleaseDate;
    private String mImagePath;

    public Movie (String Title, String ID,  String Overview, String Rating, String ReleaseDate, String Image_Path){
        mTitle =  Title;
        mID = ID;
        mOverview = Overview;
        mRating = Rating;
        mReleaseDate = ReleaseDate;
        mImagePath = Image_Path;
    }
    public String getmTitle (){
        return mTitle;
    }
    public String getmID() {
        return mID;
    }
    public String getmOverview () { return mOverview;}
    public String getmRating(){return mRating;}
    public String getmReleaseDate(){return mReleaseDate;}
    public String getmImagePath(){
    return  "http://image.tmdb.org/t/p/"+"w185"+mImagePath;
    }
}
