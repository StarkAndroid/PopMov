package com.example.serwis.popularmovies.UI;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.serwis.popularmovies.DownloadMovies;
import com.example.serwis.popularmovies.Movie;
import com.example.serwis.popularmovies.R;
import com.example.serwis.popularmovies.adapters.MoviesAdapter;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private static boolean sort_bypop=true;
    ArrayList<Movie> movies;
    MoviesAdapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.sort_bypopularity:
                if (sort_bypop==true) break;
                sort_bypop=true;
                new FetchMovies().execute(sort_bypop);
                break;
            case R.id.sort_byvote:
                if (sort_bypop==false) break;
                sort_bypop=false;
                new FetchMovies().execute(sort_bypop);
                break;
            case R.id.showfavourites:
                Intent gotofav = new Intent(MainActivity.this, FavouritesActivity.class);
                startActivity(gotofav);
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView =  findViewById(R.id.RecyclerView);

        new FetchMovies().execute(sort_bypop);


        adapter = new MoviesAdapter(movies, this);

        adapter.setOnItemClickListenerHere(new MoviesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent newIntent = new Intent(MainActivity.this, DetailActivity.class);
                Movie movie = movies.get(position);
                newIntent.putExtra("CurrentMovie", movie );
                startActivity(newIntent);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    }

    public class FetchMovies extends AsyncTask <Boolean, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(Boolean... booleans) {
            ArrayList<Movie> downloadingMovies = new ArrayList<>();

            boolean sort_bypop = booleans[0];

            try {
                downloadingMovies = new DownloadMovies().getMovies(sort_bypop);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return downloadingMovies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> downloaded_movies) {
            movies = downloaded_movies;
            adapter.newMovies(downloaded_movies);
        }
    }
}
