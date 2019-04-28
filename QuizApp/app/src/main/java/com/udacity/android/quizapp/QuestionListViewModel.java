package com.udacity.android.quizapp;

import android.app.Application;
import android.util.Log;

import com.udacity.android.quizapp.model.Question;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class QuestionListViewModel extends AndroidViewModel {

    private static final String LOGGER_TAG = "[QuestionListViewModel]";

    private LiveData<List<Question>> questionListLiveData;

    public QuestionListViewModel(Application application) {
        super(application);
        QuizDatabase database = QuizDatabase.getInstance(this.getApplication());
        Log.d(LOGGER_TAG, "Actively retrieving the question list from dataBase");
        questionListLiveData = database.quizDao().getQuestionList();
    }

    public LiveData<List<Question>> getQuestionListLiveData() {
        return questionListLiveData;
    }
}
