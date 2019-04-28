package com.udacity.android.quizapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class ResultWidgetProvider extends AppWidgetProvider {

    private static String lastResultScoreText;
    private static String lastResultInfoText;

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, String resultScoreText, String resultInfoText) {

        lastResultScoreText = resultScoreText;
        lastResultInfoText = resultInfoText;

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, ResultWidgetProvider.class));

        CharSequence widgetText = context.getString(R.string.widget_no_quiz_played);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.result_widget_provider);

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            if (lastResultScoreText != null && lastResultInfoText != null) {
                views.setTextViewText(R.id.widget_tv_result_score, lastResultScoreText);
                views.setTextViewText(R.id.widget_tv_result_info, lastResultInfoText);
            } else {
                views.setTextViewText(R.id.widget_tv_result_score, widgetText);
                views.setTextViewText(R.id.widget_tv_result_info, widgetText);
            }

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget_tv_result, pendingIntent);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, lastResultScoreText, lastResultInfoText);
        }
    }
}

