package com.example.serwis.popularmovies;

import android.net.Uri;
import android.support.v7.widget.VectorEnabledTintResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by serwis on 2018-04-14.
 */

public class DownloadTrailersAndReviews {
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API = BuildConfig.MY_API_KEY;
    private static final String API_KEY = "api_key";

    String JSONresponse;

    public ArrayList<Trailer> getTrailers (String ID) throws IOException, JSONException{

        ArrayList<Trailer> trailerArrayList = new ArrayList<Trailer>();

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(ID)
                .appendPath("videos")
                .appendQueryParameter(API_KEY, API)
                .build();

        URL url = new URL(uri.toString());

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        inputStream = urlConnection.getInputStream();
        StringBuilder builder = new StringBuilder();
        if (inputStream!=null){
            InputStreamReader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();

            while (line!=null){
                builder.append(line);
                line = bufferedReader.readLine();
            }
            JSONresponse = builder.toString();
        }

        if (!JSONresponse.isEmpty()){
            JSONObject jsonObject = new JSONObject(JSONresponse);
            JSONArray array = jsonObject.getJSONArray("results");
            int NumberofTrailers = array.length();
            if (NumberofTrailers > 5){
                NumberofTrailers = 5;
            } else if (NumberofTrailers == 0) return null;
            for (int i = 0; i < NumberofTrailers; i++){
                JSONObject jsonObject1 = array.getJSONObject(i);
                String Title = jsonObject1.getString("name");
                String Key = jsonObject1.getString("key");
                trailerArrayList.add(new Trailer(Key, Title));
            }
        }
        return trailerArrayList;
    }

    public ArrayList<String> getReviews (String ID) throws IOException, JSONException {

        ArrayList<String>  ReviewsList = new ArrayList<>();

        Uri uri = Uri.parse("https://api.themoviedb.org/3/movie").buildUpon()
                .appendPath(ID)
                .appendPath("reviews")
                .appendQueryParameter("api_key", BuildConfig.MY_API_KEY)
                .build();

        URL url = new URL(uri.toString());

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        inputStream = urlConnection.getInputStream();
        StringBuilder builder = new StringBuilder();

        if (inputStream!=null){
            InputStreamReader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();

            while (line!=null){
                builder.append(line);
                line = bufferedReader.readLine();
            }
            JSONresponse = builder.toString();
        }

        if (!JSONresponse.isEmpty()){
            JSONObject jsonObject = new JSONObject(JSONresponse);
            JSONArray array = jsonObject.getJSONArray("results");
            int NumberofReviews = array.length();
            if (NumberofReviews > 5){
                NumberofReviews = 5;
            } else if (NumberofReviews == 0) return null;
            for (int i = 0; i < NumberofReviews; i++){
                JSONObject jsonObject1 = array.getJSONObject(i);
                String Content = jsonObject1.getString("content");
                ReviewsList.add(Content);
            }
        }
        return ReviewsList;
    }
}
