package com.example.lap.popular_movies_stage1;

import android.os.Looper;
import android.os.Handler;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import androidx.annotation.NonNull;

public class MovieExecutors {
    private static MovieExecutors sInstance;
    private static final Object LOCK = new Object();
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;

    private MovieExecutors(Executor diskIO, Executor mainThread, Executor networkIO) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
        this.networkIO = networkIO;
    }

    public static MovieExecutors getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieExecutors(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
        }
        return sInstance;
    }

 public Executor diskIO(){
        return diskIO;
 }
 public Executor networkIO(){
        return networkIO ;
 }
 public Executor mainThread(){
         return mainThread ;
 }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper ());
        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post (command);
        }
    }
    }






