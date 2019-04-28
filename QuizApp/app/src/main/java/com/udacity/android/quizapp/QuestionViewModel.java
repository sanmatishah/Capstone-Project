package com.udacity.android.quizapp;

import com.udacity.android.quizapp.model.Question;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class QuestionViewModel extends ViewModel {

    private LiveData<Question> questionLiveData;

    public QuestionViewModel(QuizDatabase quizDatabase, int questionId) {
        questionLiveData = quizDatabase.quizDao().findQuestionById(questionId);
    }

    public LiveData<Question> getQuestionLiveData() {
        return questionLiveData;
    }
}
