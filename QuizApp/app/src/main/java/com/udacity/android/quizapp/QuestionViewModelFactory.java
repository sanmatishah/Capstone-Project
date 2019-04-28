package com.udacity.android.quizapp;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class QuestionViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final QuizDatabase quizDatabase;
    private final int questionId;

    public QuestionViewModelFactory(QuizDatabase quizDatabase, int questionId) {
        this.quizDatabase = quizDatabase;
        this.questionId = questionId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new QuestionViewModel(quizDatabase, questionId);
    }
}
