package com.non_name_hero.calenderview.data.source.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.non_name_hero.calenderview.data.ScheduleGroup;
import com.non_name_hero.calenderview.data.Schedule;

import java.util.List;

@Dao
public interface SchedulesDao {
    @Query("SELECT *, datetime(start_at_datetime, 'unixepoch') as start_timestamp, datetime(end_at_datetime, 'unixepoch') as end_timestamp FROM schedule ORDER BY start_at_datetime")
    List<Schedule> getAll();

    @Query("SELECT * FROM schedule WHERE schedule_id IN (:scheduleIds)")
    List<Schedule> loadSchedulesByIds(long[] scheduleIds);

    @Query("SELECT *, datetime(start_at_datetime, 'unixepoch') as start_timestamp, datetime(end_at_datetime, 'unixepoch') as end_timestamp FROM schedule WHERE start_timestamp LIKE :targetYearMonth AND end_timestamp LIKE :targetYearMonth")
    List<Schedule> findByYearMonth(String targetYearMonth);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSchedule(Schedule schedule);

    @Delete
    void delete(Schedule schedule);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertScheduleGroup(ScheduleGroup scheduleGroup);

    @Query("DELETE FROM schedule_group WHERE color_number = :colorNumber")
    void deleteScheduleGroupByColorNumber(int colorNumber);

    @Query("SELECT color_number, group_name, character_color, background_color FROM schedule_group WHERE color_number = :colorNumber")
    List<ScheduleGroup> getScheduleGroupByColorNumber(int colorNumber);

    @Query("SELECT * FROM schedule_group ORDER BY color_number DESC")
    List<ScheduleGroup> getAllScheduleGroup();
}
