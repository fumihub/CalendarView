package com.non_name_hero.calenderview.data.source.local

import androidx.room.*
import com.non_name_hero.calenderview.data.*

@Dao
interface SchedulesDao {

    /*Balance*/
    @get:Query("SELECT * FROM balance ORDER BY balance_id")
    val allBalance: List<Balance>

    @Query("SELECT * FROM balance WHERE balance_id IN (:balanceIDs)")
    fun loadBalancesByIds(balanceIDs: LongArray): List<Balance>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBalance(balance: Balance)

    @Delete
    fun deleteBalance(balance: Balance?)

    @Query("DELETE FROM balance WHERE balance_id = :balanceId")
    fun deleteByBalanceId(balanceId: Long)

    /*BalanceCategory*/
    @get:Query("SELECT * FROM balance_category ORDER BY balance_category_id")
    val allBalanceCategory: List<BalanceCategory>

    /*category*/
    @get:Query("SELECT * FROM category ORDER BY category_id")
    val allCategory: List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: Category)

    @Update
    fun updateCategory(categories: Category)

    @Delete
    fun deleteCategory(category: Category)

    /*Schedule*/
    @get:Query("SELECT * FROM schedule ORDER BY start_at_datetime, schedule_id")
    val allSchedules: List<Schedule>

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

    /*ScheduleGroup*/
    @get:Query("SELECT * FROM schedule_group ORDER BY group_id DESC")
    val allScheduleGroup: List<ScheduleGroup>

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

    /*ScheduleAndGroup*/
    @get:Query("""
        SELECT
            s.schedule_id AS scheduleId,
            s.title AS scheduleTitle,
            s.start_at_datetime AS scheduleStartAtDatetime,
            s.end_at_datetime AS scheduleEndAtDatetime,
            CASE WHEN g.group_id IS NOT NULL THEN g.group_id ELSE 1 END AS groupId,
            g.background_color AS groupTextColor,
            g.color_number AS groupColorNumber,
            g.background_color AS groupBackgroundColor
        FROM schedule s 
            LEFT JOIN schedule_group g ON g.group_id = s.group_id
        ORDER BY s.schedule_id DESC
            """)
    val allCalendarDataList: List<CalendarData>

    /*CategoryAndBalanceCategory*/
    @Query("""
        SELECT 
            c.category_id AS categoryId, 
            c.category_color AS categoryColor, 
            c.image_url AS imgURL, 
            c.big_category_name AS bigCategoryName,
            b.balance_category_id AS balanceCategoryId, 
            b.editable_flg AS editableFlg, 
            b.category_name AS categoryName
        FROM category c 
            JOIN balance_category b ON b.category_id = c.category_id
        WHERE 
            c.category_id = :categoryId
        ORDER BY c.category_id DESC
        """)
    fun getCategoryDataList(categoryId: Int): List<CategoryData>

    @Query("""
        SELECT 
            c.category_id AS categoryId, 
            c.category_color AS categoryColor, 
            c.image_url AS imgURL, 
            c.big_category_name AS bigCategoryName,
            b.balance_category_id AS balanceCategoryId, 
            b.editable_flg AS editableFlg, 
            b.category_name AS categoryName
        FROM category c 
            JOIN balance_category b ON b.category_id = c.category_id
        WHERE 
            b.balance_category_id = :balanceCategoryId
        ORDER BY c.category_id DESC
        """)
    fun getCategoryDataByBalanceCategoryId(balanceCategoryId: Int): List<CategoryData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBalanceCategory(balanceCategory: BalanceCategory)

    @Query("DELETE FROM balance_category WHERE balance_category_id = :balanceCategoryId")
    fun deleteBalanceCategoryByBalanceCategoryId(balanceCategoryId: Int)

    @Query("UPDATE balance SET balance_category_id = 1 WHERE balance_category_id = :balanceCategoryId")
    fun setDefaultBalanceCategoryId(balanceCategoryId: Int)

}
