package com.example.serwis.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.serwis.popularmovies.Movie;
import com.example.serwis.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {



    private List<Movie> movieList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public MoviesAdapter (List<Movie> movies, Context context){
        movieList = movies;
        mContext = context;
    }

    private Context getContext () {
        return mContext;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListenerHere (OnItemClickListener listenerHere){
        this.onItemClickListener = listenerHere;
    }

    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View Movie = inflater.inflate(R.layout.single_movie, parent, false);
        ViewHolder viewHolder = new ViewHolder(Movie);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.ViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        ImageView imageView = holder.imageView;
        Picasso.with(getContext()).load(movie.getmImagePath()).into(imageView);
    }

    @Override
    public int getItemCount() {
        if (movieList==null) return 0;
        else return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movieMAimage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener!=null){
                        int position = getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            onItemClickListener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }

    public void newMovies(ArrayList<Movie> movies){
        movieList = movies;
        notifyDataSetChanged();
    }
}
