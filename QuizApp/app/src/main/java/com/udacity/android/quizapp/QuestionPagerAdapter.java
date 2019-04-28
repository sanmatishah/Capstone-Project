package com.udacity.android.quizapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class QuestionPagerAdapter extends FragmentPagerAdapter {

    private final int count;

    public QuestionPagerAdapter(FragmentManager fm, int count) {
        super(fm);
        this.count = count;
    }

    @Override
    public Fragment getItem(int position) {
        return QuestionFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return count;
    }
}
