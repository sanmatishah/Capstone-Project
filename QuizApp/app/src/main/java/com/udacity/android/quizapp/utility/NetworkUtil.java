package com.udacity.android.quizapp.utility;

import android.content.Context;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class NetworkUtil {

    private static final String QUIZ_BASE_URL = "https://opentdb.com/api.php";
    private static final String QUERY_PARAM_AMOUNT = "amount";
    private static final String QUERY_PARAM_CATEGORY = "category";
    private static final String QUERY_PARAM_DIFFICULTY = "difficulty";
    private static final String QUERY_PARAM_TYPE = "type";

    private static final String QUESTION_TYPE = "multiple";

    private NetworkUtil() { }

    /**
     * Provides the URL to fetch the quiz questions.
     *
     * @return the URL to fetch the quiz questions.
     */
    @Nullable
    public static URL getUrlForQuiz(@NonNull Context context) {

        Uri.Builder buildUri = Uri.parse(QUIZ_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM_AMOUNT, PreferenceUtil.getNumberOfQuestions(context))
                .appendQueryParameter(QUERY_PARAM_CATEGORY, PreferenceUtil.getQuizCategory(context))
                .appendQueryParameter(QUERY_PARAM_DIFFICULTY, PreferenceUtil.getDifficultyLevel(context))
                .appendQueryParameter(QUERY_PARAM_TYPE, QUESTION_TYPE);

        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(@NonNull URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
