package com.udacity.android.quizapp.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.udacity.android.quizapp.R;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

public class PreferenceUtil {

    private static final String PREF_KEY_QUESTION_COUNT = "pref_question_count";
    private static final String PREF_KEY_DIFFICULTY_LEVEL = "pref_difficulty_level";
    private static final String PREF_KEY_QUIZ_CATEGORY = "pref_quiz_category";

    private PreferenceUtil() { }

    public static String getNumberOfQuestions(@NonNull Context context) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(PREF_KEY_QUESTION_COUNT, context.getString(R.string.question_count_10));
    }

    public static String getDifficultyLevel(@NonNull Context context) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(PREF_KEY_DIFFICULTY_LEVEL, context.getString(R.string.difficulty_level_value_easy));
    }

    public static String getQuizCategory(@NonNull Context context) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(PREF_KEY_QUIZ_CATEGORY, context.getString(R.string.default_quiz_category));
    }
}
