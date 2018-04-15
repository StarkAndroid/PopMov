package com.example.serwis.popularmovies.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.serwis.popularmovies.R;
import com.example.serwis.popularmovies.Trailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by serwis on 2018-04-14.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder>{

    List<Trailer> trailers;
    private Context mContext;
    private OnItemClickListener onItemClickListener;


    public TrailerAdapter (List<Trailer> trailersList, Context context ){
        trailers = trailersList;
        mContext = context;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener (OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_single, parent,
        false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);
        TextView textView = holder.textView;
        textView.setText(trailer.getmTitle());
    }

    @Override
    public int getItemCount() {
        if (trailers==null) return 0;
        else return trailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(final View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.Trailer_tv);

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
    public void swampTrailers(ArrayList<Trailer> newtrailers){
        trailers = newtrailers;
        notifyDataSetChanged();
    }
}
