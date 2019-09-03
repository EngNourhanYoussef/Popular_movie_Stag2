package com.example.lap.popular_movies_stage1;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.lap.popular_movies_stage1.Model.Movies;
import com.squareup.picasso.Picasso;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieAdapterViewHolder> {

    private Movies[] mMovieData;
    private final MovieAdapterOnClickHandler mClickHandler;

    public MoviesAdapter(Movies[] movie, MovieAdapterOnClickHandler clickHandler) {
        mMovieData = movie;
        mClickHandler = clickHandler;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(int adapterPosition);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMovieListImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieListImageView = (ImageView) itemView.findViewById(R.id.movie_posters);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.activity_sort_movies_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        //inflate list item xml into a view
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {

        String movieToBind = mMovieData[position].getPoster();
        Picasso.get()
                .load(movieToBind)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_not_found)
                .into(holder.mMovieListImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) {
            return 0;
        }
        return mMovieData.length;
    }
}

