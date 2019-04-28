package com.udacity.android.quizapp.model;

import com.google.gson.annotations.SerializedName;
import com.udacity.android.quizapp.StringListConverter;

import java.util.List;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity
@TypeConverters({StringListConverter.class})
public class Question {

    @Ignore private static final String CORRECT_ANSWER = "correct_answer";
    @Ignore private static final String INCORRECT_ANSWER_LIST = "incorrect_answers";

    @PrimaryKey private int questionId;

    @Ignore private String category = "";

//    @Ignore private String type = "";

    @Ignore private String difficulty = "";

    private String question = "";

    @SerializedName(CORRECT_ANSWER)
    private String correctAnswer = "";

    @SerializedName(INCORRECT_ANSWER_LIST)
    private List<String> incorrectAnswerList = null;

    private List<String> answerList = null;

    private boolean isAnswered = false;

    private int correctAnswerIndex = -1;

    private int selectedAnswerIndex = -1;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getIncorrectAnswerList() {
        return incorrectAnswerList;
    }

    public void setIncorrectAnswerList(List<String> incorrectAnswerList) {
        this.incorrectAnswerList = incorrectAnswerList;
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<String> answerList) {
        this.answerList = answerList;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public int getSelectedAnswerIndex() {
        return selectedAnswerIndex;
    }

    public void setSelectedAnswerIndex(int selectedAnswerIndex) {
        this.selectedAnswerIndex = selectedAnswerIndex;
    }
}
