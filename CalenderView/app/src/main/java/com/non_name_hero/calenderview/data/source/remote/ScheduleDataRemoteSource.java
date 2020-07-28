package com.non_name_hero.calenderview.data.source.remote;

import androidx.annotation.NonNull;

import com.non_name_hero.calenderview.data.Schedule;
import com.non_name_hero.calenderview.data.source.ScheduleDataSource;
import com.non_name_hero.calenderview.utils.AppExecutors;

public class ScheduleDataRemoteSource implements ScheduleDataSource {

    private static volatile ScheduleDataRemoteSource INSTANCE;

    public ScheduleDataRemoteSource (){
    }

    public static ScheduleDataRemoteSource getInstance(){
        if (INSTANCE == null){
            synchronized(ScheduleDataRemoteSource.class) {
                INSTANCE = new ScheduleDataRemoteSource();
            }
        }
        return INSTANCE;
    }

    @Override
    public void getSchedule(@NonNull long[] ScheduleIds, @NonNull GetScheduleCallback callback) {

    }

    @Override
    public void setSchedule(Schedule schedule, @NonNull SaveScheduleCallback callback) {

    }

    @Override
    public void getAllSchedules(@NonNull GetScheduleCallback callback) {

    }
}
