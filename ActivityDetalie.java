package com.example.lap.popular_movies_stage1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.lap.popular_movies_stage1.Data.FavoriteMovie;
import com.example.lap.popular_movies_stage1.Data.MovieDataBase;
import com.example.lap.popular_movies_stage1.Model.Movies;
import com.example.lap.popular_movies_stage1.Model.Review;
import com.example.lap.popular_movies_stage1.Model.Trailer;
import com.example.lap.popular_movies_stage1.utalities.JSONUtality;
import com.example.lap.popular_movies_stage1.utalities.NetworkUtality;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class ActivityDetalie extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewReviews;

    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private Movies movies;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private Trailer[] jsonTrailerData;
    private Review[] jsonReviewData;
    private int id = 0;

    private MovieDataBase mDb;
    private Boolean isFav = false;

    @BindView(R.id.movie_posters)
    ImageView Poster ;
    @BindView(R.id.overview)
    TextView OverView ;
    @BindView(R.id.tv_Title)
    TextView Movei_Name;
    @BindView(R.id.release_data)
    TextView DataRelease ;
    @BindView(R.id.tv_detail_rate)
    TextView MovieRate;
    @BindView(R.id.trailer_error_message)
    TextView mTrailerErrorMessage;
    @BindView(R.id.review_error_message)
    TextView mReviewErrorMessage;
    @BindView(R.id.add_to_favorites)
    ImageView mFavButton;
    @BindView(R.id.detail_scrollview)
    ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // favorite movies
        mFavButton = findViewById(R.id.add_to_favorites);
        mDb = MovieDataBase.getInstance(getApplicationContext());

        MovieExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final FavoriteMovie fmov = mDb.MovieDao().loadMovieById(Integer.parseInt(movies.getId()));
                setFavorite((fmov != null) ? true : false);
            }
        });
    }

    private void setFavorite(Boolean fav){
        if (fav) {
            isFav = true;
            mFavButton.setImageResource(R.drawable.ic_favorite_solid_24dp);
        } else {
            isFav = false;
            mFavButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        ButterKnife.bind(this);

      //stackoverflow.com_how-to-pass-json-image-from-recycler-view-to-another-activity
        String MovieImage = getIntent().getStringExtra("poster");
        String title = getIntent().getStringExtra("title");
        String Movierate = getIntent().getStringExtra("rate");
        String release = getIntent().getStringExtra("release");
        String overview = getIntent().getStringExtra("overview");


       Movei_Name.setText(title);
       OverView.setText(overview);
      MovieRate.setText(Movierate);
       DataRelease.setText(release);
        Picasso.get()
                .load(MovieImage)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_not_found)
                .into(Poster);

        loadTrailerData();
        loadReviewData();

    }


    private void loadTrailerData() {
        String trailerId = String.valueOf(id);
        new FetchTrailerTask().execute(trailerId);
    }

    private void loadReviewData() {
        String reviewId = String.valueOf(id);
        new FetchReviewTask().execute(reviewId);
    }

    // Async Task for trailers
    public class FetchTrailerTask extends AsyncTask<String, Void, Trailer[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Trailer[] doInBackground(String... params) {
            if (params.length == 0){
                return null;
            }

            URL movieRequestUrl = NetworkUtality.buildTrailerUrl(id);

            try {
                String jsonMovieResponse = NetworkUtality.getResponceFromHttp(movieRequestUrl);

                jsonTrailerData = JSONUtality.getTrailerInformationFromJson(ActivityDetalie.this, jsonMovieResponse);

                return jsonTrailerData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Trailer[] trailerData) {
            if (trailerData != null) {
                mTrailerAdapter = new TrailerAdapter(trailerData, ActivityDetalie.this);
                mRecyclerView.setAdapter(mTrailerAdapter);
            } else {
                mTrailerErrorMessage.setVisibility(View.VISIBLE);
            }

        }

    }


    //Async task for reviews
    public class FetchReviewTask extends AsyncTask<String, Void, Review[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Review[] doInBackground(String... params) {
            if (params.length == 0){
                return null;
            }

            URL movieRequestUrl = NetworkUtality.buildReviewUrl(id);

            try {
                String jsonMovieResponse = NetworkUtality.getResponceFromHttp(movieRequestUrl);

                jsonReviewData
                        = JSONUtality.getReviewInformationsFromJson(ActivityDetalie.this, jsonMovieResponse);

                return jsonReviewData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Review[] reviewData) {
            if (reviewData != null) {
                mReviewAdapter = new ReviewAdapter(reviewData);
                mRecyclerViewReviews.setAdapter(mReviewAdapter);
            } else {
                mReviewErrorMessage.setVisibility(View.VISIBLE);
            }
        }

    }
}


