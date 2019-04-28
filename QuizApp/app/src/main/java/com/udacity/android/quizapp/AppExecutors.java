package com.udacity.android.quizapp;

import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static final String LOGGER_TAG = "[AppExecutors]";
    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;
    private final Executor databaseIO;

    private AppExecutors(Executor databaseIO) {
        this.databaseIO = databaseIO;
    }

    public static AppExecutors getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOGGER_TAG, "Creating AppExecutors instance");
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor databaseIO() {
        return databaseIO;
    }
}
