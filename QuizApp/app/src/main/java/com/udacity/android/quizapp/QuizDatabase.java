package com.udacity.android.quizapp;

import android.content.Context;
import android.util.Log;

import com.udacity.android.quizapp.model.Question;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Question.class}, version = 1, exportSchema = false)
public abstract class QuizDatabase extends RoomDatabase {

    private static final String LOGGER_TAG = "[QuizDatabase]";
    private static final Object LOCK = new Object();
    private static final String QUIZ_DATABASE = "QUIZ_DATABASE";
    private static QuizDatabase sInstance;

    public static QuizDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOGGER_TAG, "Creating database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(), QuizDatabase.class, QUIZ_DATABASE).build();
            }
        }
        return sInstance;
    }

    public abstract QuizDao quizDao();
}
