package com.udacity.android.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.udacity.android.quizapp.customView.NonSwipeableViewPager;
import com.udacity.android.quizapp.model.Question;

import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class QuizActivity extends AppCompatActivity {

    private static final String LOGGER_TAG = "[QuizActivity]";
    private static final String DISPLAYED_QUESTION_ID = "DISPLAYED_QUESTION_ID";

    private Unbinder unbinder;

    @BindView(R.id.toolbar)
    /*package*/ Toolbar toolbar;

    @BindView(R.id.nsvp_question_container)
    /*package*/ NonSwipeableViewPager nsvpQuestionContainer;

    @BindView(R.id.button_previous)
    /*package*/ Button buttonPrevious;

    @BindView(R.id.button_next)
    /*package*/ Button buttonNext;

    @BindString(R.string.title_question)
    /*package*/ String titleQuestion;

    @BindString(R.string.message_answer_all_questions)
    /*package*/ String messageAnswerAllQuestions;

    @BindString(R.string.action_next)
    /*package*/ String actionNext;

    @BindString(R.string.action_done)
    /*package*/ String actionDone;

    private QuestionPagerAdapter mQuestionPagerAdapter;

    private QuizDatabase quizDatabase;

    private int questionCount;

    private int displayedQuestionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        unbinder = ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        displayedQuestionId = 0;
        if (savedInstanceState != null) {
            displayedQuestionId = savedInstanceState.getInt(DISPLAYED_QUESTION_ID);
        }

        QuestionListViewModel viewModel = ViewModelProviders.of(this).get(QuestionListViewModel.class);
        viewModel.getQuestionListLiveData().observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {
                if (questions != null) {
                    questionCount = questions.size();
                    if (mQuestionPagerAdapter == null) {
                        mQuestionPagerAdapter = new QuestionPagerAdapter(getSupportFragmentManager(), questionCount);
                        nsvpQuestionContainer.setAdapter(mQuestionPagerAdapter);
                        nsvpQuestionContainer.setCurrentItem(displayedQuestionId);
                    }
                    updateUI();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DISPLAYED_QUESTION_ID, displayedQuestionId);
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

    @OnClick(R.id.button_previous)
    /*package*/ void onPreviousButtonClicked() {
        nsvpQuestionContainer.setCurrentItem(--displayedQuestionId);
        updateUI();
    }

    @OnClick(R.id.button_next)
    /*package*/ void onNextButtonClicked() {
        if (displayedQuestionId == questionCount - 1) {
            if (areAllQuestionsAnswered()) {
                final Intent intent = new Intent(this, ResultActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, messageAnswerAllQuestions, Toast.LENGTH_SHORT).show();
            }
        } else {
            nsvpQuestionContainer.setCurrentItem(++displayedQuestionId);
            updateUI();
        }
    }

    private void updateUI() {
        toolbar.setTitle(String.format(Locale.getDefault(), "%s %d", titleQuestion, displayedQuestionId + 1));
        updatePreviousButton();
        updateNextButton();
    }

    private void updatePreviousButton() {
        if (buttonPrevious != null) {
            final int visibility = (displayedQuestionId == 0) ? INVISIBLE : VISIBLE;
            buttonPrevious.setVisibility(visibility);
        }
    }

    private void updateNextButton() {
        if (buttonNext != null) {
            if (displayedQuestionId == questionCount - 1) {
                buttonNext.setText(actionDone);
            } else {
                buttonNext.setText(actionNext);
            }
        }
    }

    private boolean areAllQuestionsAnswered() {
        QuestionListViewModel viewModel = ViewModelProviders.of(this).get(QuestionListViewModel.class);
        List<Question> questionList = viewModel.getQuestionListLiveData().getValue();
        if (questionList != null) {
            for (Question question : questionList) {
                if (question.getSelectedAnswerIndex() == -1) {
                    return false;
                }
            }
        }
        return true;
    }
}
