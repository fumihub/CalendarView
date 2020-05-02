package com.non_name_hero.calenderview.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
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
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager2 mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private CalendarPagerAdapter mPagerAdapter;
    private int jumpPosition = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.calendar_fragment, container, false);
        mPager = (ViewPager2) rootView.findViewById(R.id.pager);
        mPagerAdapter = new CalendarPagerAdapter(this);
        mPagerAdapter.initializeData();
        // Instantiate a ViewPager and a PagerAdapter.
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(1, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            public void onPageSelected(int position) {
                if (position == 0) {
                    jumpPosition = 3;
                    CalendarPagerAdapter adapter = (CalendarPagerAdapter) mPager.getAdapter();
                    adapter.rewindData();
                } else if (position == 4) {
                    jumpPosition = 1;
                    CalendarPagerAdapter adapter = (CalendarPagerAdapter) mPager.getAdapter();
                    adapter.forwardData();// 先ほど用意した、後データの補填 method
                }
            }

            public void onPageScrollStateChanged(int state) {
                //アニメーションが終わるのを待ってから飛ぶ。
                // onPageSelected(int position) でやると、スクロールアニメーションがキャンセルされてしまう。
                if (state == ViewPager.SCROLL_STATE_IDLE && jumpPosition >= 0) {
                    mPager.setCurrentItem(jumpPosition, false);
                    jumpPosition = -1;
                }
            }
        });
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
        private int currentMonthGap;
        private List<Integer> dateList;


        public CalendarPagerAdapter(Fragment fm) {
            super(fm);
            dateList = new ArrayList<Integer>();
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            currentMonthGap = position;
            Fragment view = new CalendarPageFragment(dateList.get(position));
            return view;
        }


        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }

        public void initializeData() {
            dateList.clear();
            for (int i = 0; i < 5; i++) {
                dateList.add(i); // 初期データの設定
            }
        }

        public void forwardData() {
            for (int i = 0; i < 3; i++) {
                dateList.remove(0);
            }
            for (int i = 0; i < 3; i++) {
                dateList.add(dateList.get(1) + i); // 後データの補填
            }
        }

        public void rewindData() {
            for (int i = 0; i > 3; i++) {
                dateList.remove(NUM_PAGES - i - 1);
            }
            for (int i = 0; i > 3; i++) {
                dateList.add(0, dateList.get(3)); // 前データの補填
            }
        }

    }

}
