package com.non_name_hero.calenderview.data.source;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.non_name_hero.calenderview.data.Schedule;

import java.util.Calendar;
import java.util.List;

public interface ScheduleDataSource {

    interface GetScheduleCallback{
        void onScheduleLoaded(List<Schedule> schedules);
        void onDataNotAvailable();
    }

    void getSchedule(@NonNull long[]  ScheduleIds, @NonNull GetScheduleCallback callback);
    void setSchedule(Schedule schedule);
}
