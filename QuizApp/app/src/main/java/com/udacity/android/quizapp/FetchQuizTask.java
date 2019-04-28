package com.udacity.android.quizapp;

import android.app.Application;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;

import com.google.gson.Gson;
import com.udacity.android.quizapp.model.Question;
import com.udacity.android.quizapp.model.Quiz;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.text.TextUtils.isEmpty;
import static com.udacity.android.quizapp.utility.NetworkUtil.getResponseFromHttpUrl;
import static com.udacity.android.quizapp.utility.NetworkUtil.getUrlForQuiz;

public class FetchQuizTask extends AsyncTask<Void, Void, String> {

    private static final String LOGGER_TAG = "[FetchQuizTask]";

    private final Application application;
    private final FetchQuizTaskCompletionHandler fetchQuizTaskCompletionHandler;

    public FetchQuizTask(@NonNull Application application, @NonNull FetchQuizTaskCompletionHandler fetchQuizTaskCompletionHandler) {
        this.application = application;
        this.fetchQuizTaskCompletionHandler = fetchQuizTaskCompletionHandler;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String jsonResponseData = null;
        final URL url = getUrlForQuiz(application.getApplicationContext());
        try {
            if (url != null) {
                jsonResponseData = getResponseFromHttpUrl(url);
            } else {
                Log.d(LOGGER_TAG, "url is null");
            }
        } catch (IOException e) {
            Log.d(LOGGER_TAG, e.getMessage());
        }

        return jsonResponseData;
    }

    @Override
    protected void onPostExecute(String jsonResponseData) {
        if (!isEmpty(jsonResponseData)) {
            final Quiz quiz = new Gson().fromJson(jsonResponseData, Quiz.class);
            AppExecutors.getInstance().databaseIO().execute(new Runnable() {
                @Override
                public void run() {
                    int i = 0;
                    QuizDatabase quizDatabase = QuizDatabase.getInstance(application.getApplicationContext());
                    quizDatabase.clearAllTables();
                    Random random = new Random();
                    for (Question question : quiz.getQuestions()) {
                        question.setQuestion(Html.fromHtml(question.getQuestion()).toString());
                        List<String> answerList = new ArrayList<>();
                        for (String answer : question.getIncorrectAnswerList()) {
                            answerList.add(Html.fromHtml(answer).toString());
                        }
                        int correctAnswerIndex = random.nextInt(4);
                        answerList.add(correctAnswerIndex, Html.fromHtml(question.getCorrectAnswer()).toString());
                        question.setCorrectAnswerIndex(correctAnswerIndex);
                        question.setAnswerList(answerList);
                        question.setQuestionId(i++);
                        quizDatabase.quizDao().insertQuestion(question);
                    }
                    fetchQuizTaskCompletionHandler.onFetchQuizTaskCompleted(jsonResponseData);
                }
            });
        } else {
            Log.d(LOGGER_TAG, "onJsonResponseAvailable > jsonResponseData is empty");
        }
    }

    interface FetchQuizTaskCompletionHandler {
        void onFetchQuizTaskCompleted(@Nullable String jsonResponseData);
    }
}
