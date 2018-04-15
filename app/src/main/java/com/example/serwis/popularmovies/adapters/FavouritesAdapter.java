package com.example.serwis.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.serwis.popularmovies.R;
import com.example.serwis.popularmovies.data.MovieContract;

/**
 * Created by serwis on 2018-04-06.
 */

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public FavouritesAdapter (Context context){
        mContext = context;
    }

    @Override
    public FavouritesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.fav_single_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavouritesAdapter.ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String Title = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_TITLE));
        holder.TitleTextView.setText(Title);
    }

    @Override
    public int getItemCount() {
        if (mCursor==null) return 0;
        else return mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView TitleTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            TitleTextView = itemView.findViewById(R.id.fav_movieTitle);
        }
    }

    public void swampCursor (Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
    }
}
