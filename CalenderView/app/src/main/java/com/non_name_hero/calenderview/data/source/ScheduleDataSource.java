package com.non_name_hero.calenderview.data.source;

import androidx.annotation.NonNull;

import com.non_name_hero.calenderview.data.Schedule;

import java.util.List;
import java.util.Map;

public interface ScheduleDataSource {

    interface GetScheduleCallback{
        void onScheduleLoaded(List<Schedule> schedules);
        void onDataNotAvailable();
    }

    interface GetScheduleMapCallback{
        void onScheduleMapLoaded(Map<String,String> scheduleStringMap);
    }

    interface SaveScheduleCallback{
        void onScheduleSaved();
        void onDataNotSaved();
    }

    void getSchedule(@NonNull long[]  ScheduleIds, @NonNull GetScheduleCallback callback);
    void setSchedule(Schedule schedule, @NonNull SaveScheduleCallback callback);
    void getAllSchedules(@NonNull GetScheduleCallback callback);
    void getHoliday(@NonNull GetScheduleCallback callback);
}
