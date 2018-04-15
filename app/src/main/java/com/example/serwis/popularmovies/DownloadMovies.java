package com.example.serwis.popularmovies;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;



public class DownloadMovies {

    private static final String VOTE_BASE_URL = "https://api.themoviedb.org/3/movie/top_rated?";
    private static final String POP_BASE_URL = "https://api.themoviedb.org/3/movie/popular?";
    //please provide your API Key in the String below
    private static final String API = BuildConfig.MY_API_KEY;
    private static final String API_KEY = "api_key";
    private static final String PAGE = "page";
    private static final String NUMBER = "1";

    String JSONresponse;

    public ArrayList<Movie> getMovies (boolean sort_bypop) throws IOException, JSONException {
        ArrayList<Movie> movies = new ArrayList<>();
        String CurrentUrl;

        if (sort_bypop){
            CurrentUrl = POP_BASE_URL;
        }
        else CurrentUrl = VOTE_BASE_URL;

        Uri uri =  Uri.parse(CurrentUrl).buildUpon()
                .appendQueryParameter(API_KEY, API)
                .appendQueryParameter(PAGE, NUMBER)
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
            JSONObject object = new JSONObject(JSONresponse);
            JSONArray array = object.getJSONArray("results");
            int numberofMovies = array.length();
            for (int i = 0; i < numberofMovies; i++){
                JSONObject object1 = array.getJSONObject(i);
                String Title = object1.getString("title");
                String ID = object1.getString("id");
                String Overview = object1.getString("overview");
                String Rating = object1.getString("vote_average");
                String Date = object1.getString("release_date");
                String Image_Path = object1.getString("poster_path");
                movies.add(new Movie(Title, ID, Overview, Rating, Date, Image_Path));
            }
        }
        return movies;
    }

}
