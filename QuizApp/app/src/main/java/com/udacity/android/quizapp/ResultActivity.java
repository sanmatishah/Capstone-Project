package com.udacity.android.quizapp;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.udacity.android.quizapp.model.Question;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.udacity.android.quizapp.ResultWidgetProvider.updateAppWidget;

public class ResultActivity extends AppCompatActivity implements FetchQuizTask.FetchQuizTaskCompletionHandler {

    private Unbinder unbinder;

    @BindView(R.id.toolbar)
    /*package*/ Toolbar toolbar;

    @BindView(R.id.tv_result_score)
    /*package*/ TextView tvResultScore;

    @BindView(R.id.tv_result_info)
    /*package*/ TextView tvResultInfo;

    @BindString(R.string.title_result)
    /*package*/ String titleResult;

    @BindString(R.string.message_score)
    /*package*/ String messageScore;

    @BindString(R.string.message_result_info_excellent)
    /*package*/ String messageResultInfoExcellent;

    @BindString(R.string.message_result_info_good)
    /*package*/ String messageResultInfoGood;

    @BindString(R.string.message_result_info_ok)
    /*package*/ String messageResultInfoOk;

    @BindString(R.string.message_result_info_never_mind)
    /*package*/ String messageResultInfoNeverMind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        unbinder = ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        QuestionListViewModel viewModel = ViewModelProviders.of(this).get(QuestionListViewModel.class);
        viewModel.getQuestionListLiveData().observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {
                viewModel.getQuestionListLiveData().removeObservers(ResultActivity.this);
                if (questions != null) {
                    populateUI(questions);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

    @OnClick(R.id.button_review)
    /*package*/ void onReviewButtonClicked() {
        final Intent intent = new Intent(this, QuizActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_play_again)
        /*package*/ void onPlayAgainButtonClicked() {
        new FetchQuizTask(getApplication(), this).execute();
    }

    @OnClick(R.id.button_end_quiz)
    /*package*/ void onEndQuizButtonClicked() {
        AppExecutors.getInstance().databaseIO().execute(new Runnable() {
            @Override
            public void run() {
                QuizDatabase.getInstance(ResultActivity.this).clearAllTables();
                final Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void populateUI(@NonNull List<Question> questions) {
        toolbar.setTitle(titleResult);

        final int questionCount = questions.size();
        final int correctAnswerCount = getCorrectAnswerCount(questions);
        final String resultScoreText = String.format(messageScore, correctAnswerCount, questionCount);
        tvResultScore.setText(resultScoreText);

        final String resultInfoText = getResultInfoForScore(correctAnswerCount, questionCount);
        tvResultInfo.setText(resultInfoText);

        updateAppWidget(getApplicationContext(), AppWidgetManager.getInstance(getApplication()), resultScoreText, resultInfoText);
    }

    private String getResultInfoForScore(int correctAnswerCount, int questionCount) {
        final float percentage = correctAnswerCount * 100.0f / questionCount;
        if (percentage >= 90.0f) {
            return messageResultInfoExcellent;
        } else if (percentage >= 75.0f) {
            return messageResultInfoGood;
        } else if (percentage >= 50.0f) {
            return messageResultInfoOk;
        }
        return messageResultInfoNeverMind;
    }

    private int getCorrectAnswerCount(@NonNull List<Question> questionList) {
        int correctAnswerCount = 0;
        for (Question question : questionList) {
            if (question.getCorrectAnswerIndex() == question.getSelectedAnswerIndex()) {
                correctAnswerCount++;
            }
        }
        return correctAnswerCount;
    }

    @Override
    public void onFetchQuizTaskCompleted(@Nullable String jsonResponseData) {
        final Intent intent = new Intent(this, QuizActivity.class);
        startActivity(intent);
    }
}
