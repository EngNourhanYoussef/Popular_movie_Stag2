package com.example.lap.popular_movies_stage1;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.Database;
import com.example.lap.popular_movies_stage1.Data.FavoriteMovie;
import com.example.lap.popular_movies_stage1.Data.MovieDataBase;

import java.util.List;

public class movieViewModel extends AndroidViewModel {
    private LiveData<List<FavoriteMovie>> movies;

    public movieViewModel(@NonNull Application application) {

        super(application);
        MovieDataBase movieDataBase = MovieDataBase.getInstance(this.getApplication());
        movies = movieDataBase.MovieDao().loadAllMovies();

    }

    public LiveData<List<FavoriteMovie>> getMovies() {
        return movies;
    }
}
