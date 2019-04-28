package com.udacity.android.quizapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.android.quizapp.model.Question;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class QuestionFragment extends Fragment {

    private static final String LOGGER_TAG = "[QuestionFragment]";
    private static final String INTENT_EXTRA_QUESTION_ID = "QUESTION_ID";

    private Unbinder unbinder;

    @BindView(R.id.tv_question)
    /*package*/ TextView tvQuestion;

    @BindView(R.id.tv_answer_a)
    /*package*/ TextView tvAnswerA;

    @BindView(R.id.tv_answer_b)
    /*package*/ TextView tvAnswerB;

    @BindView(R.id.tv_answer_c)
    /*package*/ TextView tvAnswerC;

    @BindView(R.id.tv_answer_d)
    /*package*/ TextView tvAnswerD;

    @BindDrawable(R.drawable.textview_unanswered)
    /*package*/ Drawable drawableTextViewUnanswered;

    @BindDrawable(R.drawable.textview_answer_correct)
    /*package*/ Drawable drawableTextViewAnswerCorrect;

    @BindDrawable(R.drawable.textview_answer_incorrect)
    /*package*/ Drawable drawableTextViewAnswerIncorrect;

    private QuizDatabase quizDatabase;

    private QuestionViewModelFactory questionViewModelFactory;

    public QuestionFragment() {
    }

    public static QuestionFragment newInstance(int questionId) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt(INTENT_EXTRA_QUESTION_ID, questionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_question, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        int questionId = getArguments().getInt(INTENT_EXTRA_QUESTION_ID);
        quizDatabase = QuizDatabase.getInstance(getContext());
        questionViewModelFactory = new QuestionViewModelFactory(quizDatabase, questionId);
        final QuestionViewModel viewModel = ViewModelProviders.of(this, questionViewModelFactory).get(QuestionViewModel.class);
        viewModel.getQuestionLiveData().observe(this, new Observer<Question>() {
            @Override
            public void onChanged(Question question) {
                if (question == null) {
                    Log.d(LOGGER_TAG, "Question["+questionId+"] is null");
                    return;
                }
                populateUI(question);
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroyView();
    }

    @OnClick(R.id.tv_answer_a)
        /*package*/ void onAnswerAClicked() {
            setSelectedAnswer(0);
    }

    @OnClick(R.id.tv_answer_b)
        /*package*/ void onAnswerBClicked() {
        setSelectedAnswer(1);
    }

    @OnClick(R.id.tv_answer_c)
        /*package*/ void onAnswerCClicked() {
        setSelectedAnswer(2);
    }

    @OnClick(R.id.tv_answer_d)
        /*package*/ void onAnswerDClicked() {
        setSelectedAnswer(3);
    }

    private void populateUI(@NonNull Question question) {

        tvQuestion.setText(question.getQuestion());

        List<String> answerList = question.getAnswerList();
        tvAnswerA.setText(answerList.get(0));
        tvAnswerB.setText(answerList.get(1));
        tvAnswerC.setText(answerList.get(2));
        tvAnswerD.setText(answerList.get(3));

        tvAnswerA.setBackground(drawableTextViewUnanswered);
        tvAnswerB.setBackground(drawableTextViewUnanswered);
        tvAnswerC.setBackground(drawableTextViewUnanswered);
        tvAnswerD.setBackground(drawableTextViewUnanswered);

        final int selectedAnswerIndex = question.getSelectedAnswerIndex();
        if (selectedAnswerIndex == -1) {
            tvAnswerA.setEnabled(true);
            tvAnswerB.setEnabled(true);
            tvAnswerC.setEnabled(true);
            tvAnswerD.setEnabled(true);
        } else {
            tvAnswerA.setEnabled(false);
            tvAnswerB.setEnabled(false);
            tvAnswerC.setEnabled(false);
            tvAnswerD.setEnabled(false);

            final TextView selectedAnswerTextView = getTextViewForAnswerIndex(selectedAnswerIndex);
            final int correctAnswerIndex = question.getCorrectAnswerIndex();
            if (correctAnswerIndex == selectedAnswerIndex) {
                // Selected Correct Answer
                selectedAnswerTextView.setBackground(drawableTextViewAnswerCorrect);
                selectedAnswerTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_answer_correct, 0);
            } else {
                // Selected Incorrect Answer
                selectedAnswerTextView.setBackground(drawableTextViewAnswerIncorrect);
                selectedAnswerTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_answer_incorrect, 0);

                // Show Correct Answer
                final TextView correctAnswerTextView = getTextViewForAnswerIndex(correctAnswerIndex);
                correctAnswerTextView.setBackground(drawableTextViewAnswerCorrect);
            }
        }
    }

    private TextView getTextViewForAnswerIndex(int index) {
        if (index == 0) {
            return tvAnswerA;
        } else if (index == 1) {
            return tvAnswerB;
        } else if (index == 2) {
            return tvAnswerC;
        }
        return tvAnswerD;
    }

    private void setSelectedAnswer(int index) {
        final QuestionViewModel viewModel = ViewModelProviders.of(this, questionViewModelFactory).get(QuestionViewModel.class);
        final Question question = viewModel.getQuestionLiveData().getValue();
        if (question != null) {
            question.setSelectedAnswerIndex(index);
            AppExecutors.getInstance().databaseIO().execute(new Runnable() {
                @Override
                public void run() {
                    quizDatabase.quizDao().updateQuestion(question);
                }
            });
        }
    }
}
