package com.non_name_hero.calenderview.data.source;

import androidx.annotation.NonNull;

import com.non_name_hero.calenderview.data.Schedule;
import com.non_name_hero.calenderview.data.ScheduleGroup;

import java.util.List;

public interface ScheduleDataSource {

    interface GetScheduleCallback{
        void onScheduleLoaded(List<Schedule> schedules);
        void onDataNotAvailable();
    }

    interface SaveScheduleCallback{
        void onScheduleSaved();
        void onDataNotSaved();
    }

    void getSchedule(@NonNull long[]  ScheduleIds, @NonNull GetScheduleCallback callback);
    void setSchedule(Schedule schedule, @NonNull SaveScheduleCallback callback);
    void getAllSchedules(@NonNull GetScheduleCallback callback);
    void getHoliday(@NonNull GetScheduleCallback callback);

    /**
     * ScheduleGroupのコールバック
     * onGroupSaved() -　保存成功時の処理
     * onDataNotSaved() - 保存失敗時の処理
     */
    interface SaveScheduleGroupCallback {
        void onScheduleGroupSaved();
        void onDataNotSaved();
    }

    interface GetScheduleGroupsCallback{
        void onScheduleGroupsLoaded(List<ScheduleGroup> Groups);
        void onDataNotAvailable();
    }

    interface GetScheduleGroupCallback {
        void onScheduleGroupLoaded(ScheduleGroup group);
    }

    void insertScheduleGroup(@NonNull ScheduleGroup group, @NonNull SaveScheduleGroupCallback callback);
    void deleteScheduleGroup(@NonNull int colorNumber);
    void getScheduleGroup(@NonNull int colorNumber, @NonNull GetScheduleGroupCallback callback);
    void getListScheduleGroup(@NonNull GetScheduleGroupsCallback callback);
}
