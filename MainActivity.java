package com.example.lap.popular_movies_stage1;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.lap.popular_movies_stage1.Data.FavoriteMovie;
import com.example.lap.popular_movies_stage1.Model.Movies;
import com.example.lap.popular_movies_stage1.utalities.JSONUtality;
import com.example.lap.popular_movies_stage1.utalities.NetworkUtality;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieAdapterOnClickHandler {

        private RecyclerView mRecyclerView;
        private MoviesAdapter mMovieAdapter;
        private Movies[] jsonMovieData;
        private List<FavoriteMovie> favMovs;


        @BindView(R.id.tv_connection_error) TextView mConnctionError ;
        @BindView(R.id.Loading_Page) ProgressBar mLoadingPage;

        String query = "popular";
        private static final String TAG = MainActivity.class.getSimpleName();;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

            ButterKnife.bind(this);


            int mNoOfColumns = calculateNoOfColumns(getApplicationContext());

            GridLayoutManager layoutManager = new GridLayoutManager(this, mNoOfColumns);

            mRecyclerView.setLayoutManager(layoutManager);

            mRecyclerView.setHasFixedSize(true);

            mRecyclerView.setAdapter(mMovieAdapter);

            favMovs = new ArrayList<FavoriteMovie>();

            setTitle(getString(R.string.app_name) + " - Popular");

            setupViewModel();

            loadMovieData();
        }

        private void loadMovieData() {
            String theMovieDbQueryType = query;
            showJsonDataResults();
            new FetchMovieTask().execute(theMovieDbQueryType);
        }

        @Override
        public void onClick(int adapterPosition) {
            Context context = this;
            Class destinationClass = ActivityDetalie.class;

            Intent intentToStartDetailActivity = new Intent(context, destinationClass);
            intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, adapterPosition);
            intentToStartDetailActivity.putExtra("title", jsonMovieData[adapterPosition].getTitle());
            intentToStartDetailActivity.putExtra("poster", jsonMovieData[adapterPosition].getPoster());
            intentToStartDetailActivity.putExtra("rate", jsonMovieData[adapterPosition].getRate());
            intentToStartDetailActivity.putExtra("release", jsonMovieData[adapterPosition].getRelease());
            intentToStartDetailActivity.putExtra("overview", jsonMovieData[adapterPosition].getOverview());
            intentToStartDetailActivity.putExtra("id", jsonMovieData[adapterPosition].getId());

            startActivity(intentToStartDetailActivity);
        }

        private void showJsonDataResults() {
            mConnctionError = (TextView) findViewById(R.id.tv_connection_error);
            mConnctionError.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        private void showErrorMessage() {
            mConnctionError = (TextView) findViewById(R.id.tv_connection_error);
            mRecyclerView.setVisibility(View.INVISIBLE);
            mConnctionError.setVisibility(View.VISIBLE);
        }

        public class FetchMovieTask extends AsyncTask<String, Void, Movies[]> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mLoadingPage = (ProgressBar) findViewById(R.id.Loading_Page);
                mLoadingPage.setVisibility(View.VISIBLE);
            }

            @Override
            protected Movies[] doInBackground(String... params) {
                if (params.length == 0){
                    return null;
                }

                String sortBy = params[0];
                URL movieRequestUrl = NetworkUtality.buildUrl(sortBy);

                try {
                    String jsonMovieResponse = NetworkUtality.getResponceFromHttp(movieRequestUrl);

                   jsonMovieData = JSONUtality.getMovieInformationsFromJson(MainActivity.this, jsonMovieResponse);
                    return jsonMovieData;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Movies[] movieData) {
                mLoadingPage.setVisibility(View.INVISIBLE);
                if (movieData != null) {
                    showJsonDataResults();
                    mMovieAdapter = new MoviesAdapter(movieData,MainActivity.this);
                    mRecyclerView.setAdapter(mMovieAdapter);
                } else {
                    showErrorMessage();
                }
            }

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int menuItemSelected = item.getItemId();

            if (menuItemSelected == R.id.action_popular) {
                query = "popular";
                loadMovieData();
                return true;
            }

            if (menuItemSelected == R.id.action_top_rated) {
                query = "top_rated";
                loadMovieData();
                return true;
            }


            return super.onOptionsItemSelected(item);
        }


    private void setupViewModel() {
        movieViewModel viewModel = ViewModelProviders.of(this).get(movieViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(@NonNull List<FavoriteMovie> favs) {
                if(favs.size()>0) {
                    favMovs.clear();
                    favMovs = favs;
                }
                for (int i=0; i<favMovs.size(); i++) {
                    Log.d(TAG,favMovs.get(i).getTitle());
                }
                loadMovieData();
            }
        });
    }
        //calculates how many columns can I fit in screen.
        //Source: https://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns
        public static int calculateNoOfColumns(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            int noOfColumns = (int) (dpWidth / 180);
            return noOfColumns;
        }

    }
