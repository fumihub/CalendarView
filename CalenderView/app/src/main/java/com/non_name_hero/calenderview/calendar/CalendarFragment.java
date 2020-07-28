package com.non_name_hero.calenderview.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.non_name_hero.calenderview.R;

import java.util.ArrayList;
import java.util.List;

public class CalendarFragment extends Fragment implements CalendarContract.View {

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    public CalendarContract.Presenter mPresenter;

    private static final int NUM_PAGES = 100;
    private ViewPager2 mPager;
    private CalendarPagerAdapter mPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.calendar_fragment, container, false);
        mPager = (ViewPager2) rootView.findViewById(R.id.pager);
        mPagerAdapter = new CalendarPagerAdapter(this);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        mPager.setCurrentItem(50, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void setPresenter(CalendarContract.Presenter presenter) {
        mPresenter = presenter;

    }
/*
    @Override
    public void onBackPressed() {
        //TODO 要調整
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }*/

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class CalendarPagerAdapter extends FragmentStateAdapter {

        public CalendarPagerAdapter(Fragment f) {
            super(f);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment view = new CalendarPageFragment(position - 50);
            ((CalendarPageFragment) view).setPresenter(mPresenter);
            return view;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }

    }
}
