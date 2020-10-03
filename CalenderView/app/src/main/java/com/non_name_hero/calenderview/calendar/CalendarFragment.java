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

import java.util.Calendar;

import static com.non_name_hero.calenderview.utils.ActivityUtils.addFragmentToActivity;

public class CalendarFragment extends Fragment {

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    private static final int NUM_PAGES = 100;
    private static final int DEFAULT_PAGE = NUM_PAGES / 2;
    private static final int MAX_MONTH = 12;
    private ViewPager2 mPager;
    private CalendarPagerAdapter mPagerAdapter;
    private CalendarViewModel mViewModel;
    private Calendar mCalendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCalendar = Calendar.getInstance();
        View rootView = inflater.inflate(R.layout.calendar_fragment, container, false);
        //CalendarViewModelを取得
        mViewModel = MainActivity.obtainViewModel(getActivity());
        //データを更新
        loadData();

        mPager = (ViewPager2) rootView.findViewById(R.id.pager);
        mPagerAdapter = new CalendarPagerAdapter(this);

        mPager.setOffscreenPageLimit(5);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(DEFAULT_PAGE, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                //現在の月をviewModelにセット矢印DataBindingでtoolbarに表示
                mViewModel.setCurrentMonth(getCurrentMonth(position));
            }
        });
        initScheduleList();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.reloadCalendarData(true);
    }

    private void loadData() {
        mViewModel.start();
    }

    private int getCurrentMonth(int position) {
        int nowMonth = mCalendar.get(Calendar.MONTH);// 0~11
        int offset = position - DEFAULT_PAGE;
        //offset -> 現在ページからの差分
        //monthOffset -> 現在月からの差分
        int monthOffset;
        monthOffset = Math.abs(offset) % MAX_MONTH;
        //マイナスの場合は12から差をとる
        if (offset < 0) monthOffset = MAX_MONTH - monthOffset;
        // n月からの差分[-n 〜 n] + n % 12 + 1 → 1~12
        return (monthOffset + nowMonth) % MAX_MONTH + 1;
    }

    private void initScheduleList() {
        //ScheduleListを表示
        ScheduleListFragment scheduleListFragment = (ScheduleListFragment) getChildFragmentManager().findFragmentById(R.id.schedule_list_fragment_container);
        if (scheduleListFragment == null) {
            scheduleListFragment = ScheduleListFragment.newInstance();
            addFragmentToActivity(getChildFragmentManager(), scheduleListFragment, R.id.schedule_list_fragment_container);
        }
    }

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
            Fragment view = new CalendarPageFragment(position - DEFAULT_PAGE);
            return view;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }

    }
}
