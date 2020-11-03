package com.non_name_hero.calenderview.data.source.local

import androidx.room.*
import com.non_name_hero.calenderview.data.CalendarData
import com.non_name_hero.calenderview.data.Schedule
import com.non_name_hero.calenderview.data.ScheduleGroup

@Dao
interface SchedulesDao {
    /*
    * Schedule
    * */
    @get:Query("SELECT * FROM schedule ORDER BY start_at_datetime, schedule_id")
    val all: List<Schedule>

    @Query("SELECT * FROM schedule WHERE schedule_id IN (:scheduleIds)")
    fun loadSchedulesByIds(scheduleIds: LongArray): List<Schedule>

    @Query("SELECT * FROM schedule WHERE start_at_datetime >= :targetYearMonth AND end_at_datetime <= :targetYearMonth")
    fun findByYearMonth(targetYearMonth: String): List<Schedule>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchedule(schedule: Schedule)

    @Delete
    fun delete(schedule: Schedule?)

    @Query("DELETE FROM schedule WHERE schedule_id = :scheduleId")
    fun deleteByScheduleId(scheduleId: Long)

    @Query("UPDATE schedule SET group_id = 1 WHERE group_id = :groupId")
    fun setDefaultGroupId(groupId: Int)

    /*
    ScheduleGroup
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertScheduleGroup(scheduleGroup: ScheduleGroup)

    @Update
    fun updateScheduleGroup(groups: ScheduleGroup)

    @Delete
    fun deleteScheduleGroup(group: ScheduleGroup)

    @Query("DELETE FROM schedule_group WHERE group_id = :groupId")
    fun deleteScheduleGroupByColorNumber(groupId: Int)

    @Query("SELECT group_id, color_number, group_name, character_color, background_color FROM schedule_group WHERE color_number = :colorNumber")
    fun getScheduleGroupByColorNumber(colorNumber: Int): List<ScheduleGroup>

    @get:Query("SELECT * FROM schedule_group ORDER BY group_id DESC")
    val allScheduleGroup: List<ScheduleGroup>

    /*ScheduleAndGroup*/
    @get:Query("SELECT s.schedule_id AS scheduleId, s.title AS scheduleTitle, s.start_at_datetime AS scheduleStartAtDatetime, s.end_at_datetime AS scheduleEndAtDatetime, CASE WHEN g.group_id IS NOT NULL THEN g.group_id ELSE 1 END AS groupId, " +
            "g.background_color AS groupTextColor, g.color_number AS groupColorNumber, g.background_color AS groupBackgroundColor " +
            "FROM schedule s " +
            "LEFT JOIN schedule_group g ON g.group_id = s.group_id " +
            "ORDER BY s.schedule_id DESC")
    val allCalendarDataList: List<CalendarData>
}