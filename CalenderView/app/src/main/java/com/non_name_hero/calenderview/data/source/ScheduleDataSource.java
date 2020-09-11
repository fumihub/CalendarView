package com.non_name_hero.calenderview.data.source;

import androidx.annotation.NonNull;

import com.non_name_hero.calenderview.data.CalendarData;
import com.non_name_hero.calenderview.data.Schedule;
import com.non_name_hero.calenderview.data.ScheduleGroup;

import java.util.List;
import java.util.Map;

public interface ScheduleDataSource {

    interface GetScheduleCallback{
        void onScheduleLoaded(List<Schedule> schedules);
        void onDataNotAvailable();
    }

    interface GetScheduleMapCallback{
        void onScheduleMapLoaded(Map<String, List<Schedule>> scheduleStringMap);
    }

    interface SaveScheduleCallback{
        void onScheduleSaved();
        void onDataNotSaved();
    }

    void getSchedule(@NonNull long[]  ScheduleIds, @NonNull GetScheduleCallback callback);
    void setSchedule(Schedule schedule, @NonNull SaveScheduleCallback callback);
    void getAllSchedules(@NonNull GetScheduleCallback callback);


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
    void updateScheduleGroup(@NonNull ScheduleGroup group, @NonNull SaveScheduleGroupCallback callback);

    interface LoadHolidayCalendarDataCallback {
        void onHolidayCalendarDataLoaded(List<CalendarData> calendarDataList);
        void onDataNotAvailable();
    }

    interface LoadCalendarDataCallback {
        void onCalendarDataLoaded(List<CalendarData> calendarDataList);
        void onDataNotAvailable();
    }

    interface GetCalendarDataMapCallback {
        void onCalendarDataMapLoaded(Map<String, List<CalendarData>> calendarDataMap);
        void onDataNotAvailable();
    }

    void getHoliday(@NonNull LoadHolidayCalendarDataCallback callback);
    void getCalendarDataList(@NonNull LoadCalendarDataCallback callback);
}
