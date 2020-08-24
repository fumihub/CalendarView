package com.non_name_hero.calenderview.calendar;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.non_name_hero.calenderview.data.Schedule;
import com.non_name_hero.calenderview.data.source.ScheduleDataSource;
import com.non_name_hero.calenderview.data.source.ScheduleRepository;

import java.util.List;

import static androidx.core.util.Preconditions.checkNotNull;


public class CalendarPresenter implements CalendarContract.Presenter {
    private final CalendarContract.View mCalendarView;
    private final ScheduleRepository mScheduleRepository;


    @SuppressLint("RestrictedApi")
    public CalendarPresenter(@NonNull CalendarContract.View calendarFragment, @NonNull ScheduleRepository scheduleRepository){
        mCalendarView = checkNotNull(calendarFragment);
        mScheduleRepository = checkNotNull(scheduleRepository);
        mCalendarView.setPresenter(this);
    }
    @Override
    public void start() {

    }

    @Override
    public void setCurrentMonth(String mCurrentMonth) {

    }

    public void getSchedules(){
        mScheduleRepository.getAllSchedules(new ScheduleDataSource.GetScheduleCallback() {
            @Override
            public void onScheduleLoaded(List<Schedule> schedules) {
                for (Schedule s :schedules){
                    Log.d(s.getStartTimestamp(),s.getTitle());
                }
            }

            @Override
            public void onDataNotAvailable() {
                //TODO 失敗時の処理を記載
            }
        });
    }

    @Override
    public void createdCalendar() {
        mScheduleRepository.getHoliday(new ScheduleDataSource.GetScheduleCallback() {
            @Override
            public void onScheduleLoaded(List<Schedule> schedules) {
                Log.d("tag","holiday");
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }
}
