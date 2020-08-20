package com.non_name_hero.calenderview.calendar;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.non_name_hero.calenderview.data.source.ScheduleRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static androidx.core.util.Preconditions.checkNotNull;


public class CalendarPresenter implements CalendarContract.Presenter {
    private final CalendarContract.View mCalendarView;
    private final ScheduleRepository mScheduleRepository;
    private HashMap<Date,String> mScheduleMap;
    private SimpleDateFormat formatYYYYMMDD = new SimpleDateFormat("yyyy.MM.dd", Locale.US);


    @SuppressLint("RestrictedApi")
    public CalendarPresenter(@NonNull CalendarContract.View calendarFragment, @NonNull ScheduleRepository scheduleRepository){
        mCalendarView = checkNotNull(calendarFragment);
        mScheduleRepository = checkNotNull(scheduleRepository);
        mCalendarView.setPresenter(this);
    }
    @Override
    public void start() {

    }

}
