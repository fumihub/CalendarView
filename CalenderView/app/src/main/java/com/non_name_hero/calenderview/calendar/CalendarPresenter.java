package com.non_name_hero.calenderview.calendar;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.util.Date;

import static com.google.android.gms.internal.ads.zzdlg.checkNotNull;


public class CalendarPresenter implements CalendarContract.Presenter {
    private final CalendarContract.View mCalendarView;


    public CalendarPresenter(@NonNull CalendarContract.View calendarFragment){
        mCalendarView = checkNotNull(calendarFragment);
        mCalendarView.setPresenter(this);
    }
    @Override
    public void start() {

    }
}
