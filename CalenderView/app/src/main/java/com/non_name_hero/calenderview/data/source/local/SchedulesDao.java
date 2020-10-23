package com.non_name_hero.calenderview.data.source.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.non_name_hero.calenderview.data.CalendarData;
import com.non_name_hero.calenderview.data.Schedule;
import com.non_name_hero.calenderview.data.ScheduleGroup;

import java.util.List;

@Dao
public interface SchedulesDao {
    /*
    * Schedule
    * */
    @Query("SELECT * FROM schedule ORDER BY start_at_datetime, schedule_id")
    List<Schedule> getAll();

    @Query("SELECT * FROM schedule WHERE schedule_id IN (:scheduleIds)")
    List<Schedule> loadSchedulesByIds(long[] scheduleIds);

    @Query("SELECT * FROM schedule WHERE start_at_datetime >= :targetYearMonth AND end_at_datetime <= :targetYearMonth")
    List<Schedule> findByYearMonth(String targetYearMonth);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSchedule(Schedule schedule);

    @Delete
    void delete(Schedule schedule);

    @Query("DELETE FROM schedule WHERE schedule_id = :scheduleId")
    void deleteByScheduleId(long scheduleId);

    @Query("UPDATE schedule SET group_id = 1 WHERE group_id = :groupId")
    void setDefaultGroupId(int groupId);

    /*
    ScheduleGroup
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertScheduleGroup(ScheduleGroup scheduleGroup);

    @Update
    void updateScheduleGroup(ScheduleGroup groups);

    @Delete
    void deleteScheduleGroup(ScheduleGroup group);

    @Query("DELETE FROM schedule_group WHERE group_id = :groupId")
    void deleteScheduleGroupByColorNumber(int groupId);

    @Query("SELECT group_id, color_number, group_name, character_color, background_color FROM schedule_group WHERE color_number = :colorNumber")
    List<ScheduleGroup> getScheduleGroupByColorNumber(int colorNumber);

    @Query("SELECT * FROM schedule_group ORDER BY group_id DESC")
    List<ScheduleGroup> getAllScheduleGroup();

    /*ScheduleAndGroup*/
    @Query("SELECT s.schedule_id AS scheduleId, s.title AS scheduleTitle, s.start_at_datetime AS scheduleStartAtDatetime, s.end_at_datetime AS scheduleEndAtDatetime, CASE WHEN g.group_id IS NOT NULL THEN g.group_id ELSE 1 END AS groupId, " +
            "g.background_color AS groupTextColor, g.color_number AS groupColorNumber, g.background_color AS groupBackgroundColor " +
            "FROM schedule s " +
            "LEFT JOIN schedule_group g ON g.group_id = s.group_id " +
            "ORDER BY s.schedule_id DESC")
    List<CalendarData> getAllCalendarDataList();

}

