package com.udacity.android.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements FetchQuizTask.FetchQuizTaskCompletionHandler {

    private static final String LOGGER_TAG = "[MainActivity]";

    private Unbinder unbinder;

    private Tracker tracker;

    @BindView(R.id.toolbar)
    /*package*/ Toolbar toolbar;

    @BindView(R.id.publisherAdView)
    /*package*/ PublisherAdView mPublisherAdView;

    @BindString(R.string.action_play_quiz)
    /*package*/ String actionPlayQuiz;

    @BindString(R.string.message_play_quiz_clicked)
    /*package*/ String playQuizClicked;

    @BindString(R.string.analytics_category_quiz_count)
    /*package*/ String analyticsCategoryQuizCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();

        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        mPublisherAdView.loadAd(adRequest);

        QuizDatabase.getInstance(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            final Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.button_play_quiz)
    /*package*/ void onPlayQuizButtonClicked() {
        tracker.setScreenName("MainActivity");
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(analyticsCategoryQuizCount)
                .setAction(actionPlayQuiz)
                .setLabel(actionPlayQuiz)
                .setValue(1)
                .build());
        new FetchQuizTask(getApplication(), this).execute();
        Toast.makeText(this, playQuizClicked, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFetchQuizTaskCompleted(@Nullable String jsonResponseData) {
        final Intent intent = new Intent(this, QuizActivity.class);
        startActivity(intent);
    }
}
