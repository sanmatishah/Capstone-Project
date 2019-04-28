package com.udacity.android.quizapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Quiz {

    private static final String RESPONSE_CODE = "response_code";
    private static final String RESULTS = "results";

    @SerializedName(RESPONSE_CODE)
    private int responseCode = -1;

    @SerializedName(RESULTS)
    private List<Question> questions = null;

    public int getResponseCode() {
        return responseCode;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}

