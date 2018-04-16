package com.example.serwis.popularmovies.UI;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.serwis.popularmovies.BuildConfig;
import com.example.serwis.popularmovies.DownloadTrailersAndReviews;
import com.example.serwis.popularmovies.Movie;
import com.example.serwis.popularmovies.R;
import com.example.serwis.popularmovies.Trailer;
import com.example.serwis.popularmovies.adapters.TrailerAdapter;
import com.example.serwis.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by serwis on 2018-03-13.
 */

public class DetailActivity extends AppCompatActivity{

    ImageView Poster;
    TextView Title;
    TextView Overview;
    TextView Rating;
    TextView ReleaseDate;

    TrailerAdapter trailerAdapter;
    ArrayAdapter<String> StringAdapter;
    Movie CurrentMovie;
    ArrayList<Trailer> trailers;
    ArrayList<String> Reviews;
    NestedScrollView nestedScrollView;

    String PositionArray = "POSITION_ARRAY";
    String TrailersKey = "TRAILERS";
    String ReviewsKey = "REVIEWS";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        nestedScrollView = findViewById(R.id.NSW);

        Context context = getApplicationContext();
        Intent newIntent = getIntent();
        CurrentMovie = (Movie) newIntent.getSerializableExtra("CurrentMovie");
        Title = findViewById(R.id.TitletextView);
        Poster = findViewById(R.id.DetailimageView);
        Overview = findViewById(R.id.OvervieTextView4);
        Rating = findViewById(R.id.RatingTextView2);
        ReleaseDate = findViewById(R.id.DateTextView3);
        ReleaseDate.setText(getString(R.string.release_dateIntro)+ " " + CurrentMovie.getmReleaseDate());
        Rating.setText(getString(R.string.title_rating)+ " " + CurrentMovie.getmRating());
        Title.setText(CurrentMovie.getmTitle());
        Overview.setText(CurrentMovie.getmOverview());
        Picasso.with(context).load(CurrentMovie.getmImagePath()).into(Poster);

        RecyclerView recyclerView = findViewById(R.id.trailers_rv);
        trailerAdapter = new TrailerAdapter(trailers, this);
        trailerAdapter.setOnItemClickListener(new TrailerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Uri webpage = Uri.parse("https://www.youtube.com/watch?v="+trailers.get(position).getmKey());
                Intent newIntent = new Intent(Intent.ACTION_VIEW, webpage);
                if (newIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(newIntent);
            }}
        });

        recyclerView.setAdapter(trailerAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false));
        recyclerView.setNestedScrollingEnabled(false);

        ListView listView = findViewById(R.id.reviews_LV);

        Reviews = new ArrayList<>();
        StringAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Reviews);
        listView.setAdapter(StringAdapter);


        if(savedInstanceState!=null){
            trailers = savedInstanceState.getParcelableArrayList(TrailersKey);
            trailerAdapter.swampTrailers(trailers);
            Reviews = savedInstanceState.getStringArrayList(ReviewsKey);
            StringAdapter.addAll(Reviews);
            StringAdapter.notifyDataSetChanged();
            final int[] position = savedInstanceState.getIntArray(PositionArray);
            if(position != null)
                nestedScrollView.post(new Runnable() {
                    public void run() {
                        nestedScrollView.scrollTo(position[0], position[1]);
                    }
                });


        } else {new GainReviewsAndTrailers().execute(CurrentMovie.getmID());
            new GainReviews().execute(CurrentMovie.getmID());
        }
    }

    public void insert (View view){

        String[] a = new String[1];
        a[0] = CurrentMovie.getmID();
        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath("1").build(), null, null, a, null, null);

        if (cursor.getCount()==0){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.MOVIE_ID, CurrentMovie.getmID());
        contentValues.put(MovieContract.MovieEntry.MOVIE_TITLE, CurrentMovie.getmTitle());

        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

        }else if (cursor.getCount()==1){
            Toast.makeText(getBaseContext(), R.string.maniac, Toast.LENGTH_LONG).show();
        }
    }

    public class GainReviewsAndTrailers extends AsyncTask<String, Void, ArrayList<Trailer>>{

        @Override
        protected ArrayList<Trailer> doInBackground(String... ID) {
            ArrayList<Trailer> TrailersArrayList = new ArrayList<>();

            try {
                TrailersArrayList = new DownloadTrailersAndReviews().getTrailers(ID[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return TrailersArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> new_trailers) {
            trailers = new_trailers;
            trailerAdapter.swampTrailers(trailers);
        }
    }

    public class GainReviews extends AsyncTask<String, Void, ArrayList<String>>{

        @Override
        protected ArrayList<String> doInBackground(String... ID) {
            ArrayList<String> ReviewsList = new ArrayList<>();
            try {
                ReviewsList = new DownloadTrailersAndReviews().getReviews(ID[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return ReviewsList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> ReviewsList) {
            if (ReviewsList!=null){
                Reviews = ReviewsList;
            StringAdapter.addAll(Reviews);
            StringAdapter.notifyDataSetChanged();}
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(PositionArray, new int[]{nestedScrollView.getScrollX(),
                nestedScrollView.getScrollY()});
        outState.putParcelableArrayList(TrailersKey, trailers);
        outState.putStringArrayList(ReviewsKey, Reviews);
    }
}
