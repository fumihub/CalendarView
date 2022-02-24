package com.non_name_hero.calenderview.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.non_name_hero.calenderview.data.*
import java.util.*

@Dao
interface SchedulesDao {

    /*Schedule*/
    @get:Query("SELECT * FROM schedule ORDER BY start_at_datetime, schedule_id")
    val allSchedules: List<Schedule>

    @Query("SELECT * FROM schedule WHERE schedule_id IN (:scheduleIds)")
    fun loadSchedulesByIds(scheduleIds: LongArray): List<Schedule>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchedule(schedule: Schedule)

    @Delete
    fun delete(schedule: Schedule?)

    @Query("DELETE FROM schedule WHERE schedule_id = :scheduleId")
    fun deleteByScheduleId(scheduleId: Long)

    @Query("SELECT * FROM schedule WHERE start_at_datetime >= :targetStartDate AND end_at_datetime <= :targetEndDate")
    fun pickUpSchedules(targetStartDate: Date, targetEndDate: Date): List<Schedule>


    /*ScheduleGroup*/
    @get:Query("SELECT * FROM schedule_group ORDER BY group_id DESC")
    val allScheduleGroup: List<ScheduleGroup>

    /*同名の色グループが存在していなければ、色グループ追加(大文字小文字区別なし)*/
    /*戻り値：primaryKey*/
    @Query("INSERT INTO schedule_group(color_number, group_name, character_color, background_color) SELECT :colorNumber, :colorCreateTitle, :textColor, :color WHERE NOT EXISTS (SELECT 1 FROM schedule_group WHERE group_name like :colorCreateTitle)")
    fun insertScheduleGroup(colorNumber: Int, colorCreateTitle: String, textColor: String, color: Int): Long

    /*同名の色グループが存在していなければ、色グループを編集(大文字小文字区別なし)*/
    /*戻り値：更新された行数=groupIdが一致するものだけのため「0」か「1」*/
    @Query("UPDATE schedule_group SET color_number = :colorNumber, group_name = :colorCreateTitle, character_color = :textColor, background_color = :color WHERE group_id = :groupId AND NOT EXISTS (SELECT 1 FROM schedule_group WHERE group_name like :colorCreateTitle)")
    fun updateScheduleGroup(groupId: Int, colorNumber: Int, colorCreateTitle: String, textColor: String, color: Int): Int

    @Delete
    fun deleteScheduleGroup(group: ScheduleGroup)

    @Query("DELETE FROM schedule_group WHERE group_id = :groupId")
    fun deleteScheduleGroupByColorNumber(groupId: Int)

    @Query("SELECT group_id, color_number, group_name, character_color, background_color FROM schedule_group WHERE color_number = :colorNumber")
    fun getScheduleGroupByColorNumber(colorNumber: Int): List<ScheduleGroup>

    @Query("UPDATE schedule SET group_id = 1 WHERE group_id = :groupId")
    fun setDefaultGroupId(groupId: Int)


    /*ScheduleAndGroup*/
    @get:Query("""
        SELECT
            s.schedule_id AS scheduleId,
            s.title AS scheduleTitle,
            s.start_at_datetime AS scheduleStartAtDatetime,
            s.end_at_datetime AS scheduleEndAtDatetime,
            CASE WHEN g.group_id IS NOT NULL THEN g.group_id ELSE 1 END AS groupId,
            g.character_color AS groupTextColor,
            g.color_number AS groupColorNumber,
            g.background_color AS groupBackgroundColor
        FROM schedule s 
            JOIN schedule_group g ON g.group_id = s.group_id
        ORDER BY s.schedule_id DESC
            """)
    val allCalendarDataList: List<CalendarData>

    /**
     * 指定月のスケジュールと家計簿を取得
     */
    @Query("""
        SELECT
            s.schedule_id AS scheduleId,
            s.title AS scheduleTitle,
            s.start_at_datetime AS scheduleStartAtDatetime,
            s.end_at_datetime AS scheduleEndAtDatetime,
            CASE WHEN g.group_id IS NOT NULL THEN g.group_id ELSE 1 END AS groupId,
            g.character_color AS groupTextColor,
            g.color_number AS groupColorNumber,
            g.background_color AS groupBackgroundColor
        FROM schedule s 
            JOIN schedule_group g ON g.group_id = s.group_id
        WHERE 
            s.start_at_datetime < :startMonth
            and s.end_at_datetime > :endMonth
        ORDER BY s.schedule_id DESC
            """)
    fun getCalendarDataListByMonthPeriod(startMonth: Date, endMonth: Date): LiveData<List<CalendarData>>

    /**
     * TODO: 月指定で取得する
     * -- WHERE
     * -- b.used_at_datetime >= :startMonth
     * -- and b.used_at_datetime <= :endMonth
     */
    @Query("""
        SELECT
            b.timestamp AS timestamp,
            c.balance_type AS balanceType,
            sum(b.price) AS price,
            count(*) AS count
        FROM balance b
            JOIN balance_category bc ON bc.balance_category_id = b.balance_category_id
            JOIN category c ON bc.category_id = c.category_id
        WHERE
            b.used_at_datetime >= :startMonth
            and b.used_at_datetime <= :endMonth
        GROUP BY timestamp, c.balance_type
        ORDER BY b.timestamp, c.balance_type
            """)
    fun getBalanceDataListByMonthPeriod(startMonth: Date, endMonth: Date): List<BalanceData>

    @Query("""
        SELECT
            b.timestamp AS timestamp,
            c.balance_type AS balanceType,
            sum(b.price) AS price,
            count(*) AS count
        FROM balance b
            JOIN balance_category bc ON bc.balance_category_id = b.balance_category_id
            JOIN category c ON bc.category_id = c.category_id
        GROUP BY timestamp, c.balance_type
        ORDER BY b.timestamp, c.balance_type
            """)
    fun getBalanceDataList(): List<BalanceData>

    /*CategoryAndBalanceCategory*/
    /*CategoryAndBalanceCategoryの全ての要素をリストとして取り出す*/
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

    /*CategoryAndBalanceCategoryから１つの要素をリストとして取り出す*/
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


    /*Balance*/
    @get:Query("SELECT * FROM balance ORDER BY balance_id")
    val allBalance: List<Balance>

    @Query("SELECT * FROM balance WHERE balance_id IN (:balanceIds)")
    fun loadBalancesByIds(balanceIds: LongArray): List<Balance>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBalance(balance: Balance)

    @Delete
    fun deleteBalance(balance: Balance?)

    @Query("DELETE FROM balance WHERE balance_id = :balanceId")
    fun deleteByBalanceId(balanceId: Long)


    /*BalanceCategory*/
    @get:Query("SELECT * FROM balance_category ORDER BY balance_category_id")
    val allBalanceCategory: List<BalanceCategory>

    /*同名のカテゴリーが存在していなければ、カテゴリー追加(大文字小文字区別なし)*/
    /*戻り値：primaryKey*/
    @Query("INSERT INTO balance_category(editable_flg, category_name, category_id) SELECT :editFlag, :balanceCategoryName, :categoryId WHERE NOT EXISTS (SELECT 1 FROM balance_category WHERE category_name like :balanceCategoryName)")
    fun insertBalanceCategory(editFlag: Boolean, balanceCategoryName: String, categoryId: Int): Long

    @Query("DELETE FROM balance_category WHERE balance_category_id = :balanceCategoryId")
    fun deleteBalanceCategoryByBalanceCategoryId(balanceCategoryId: Int)

    /*削除されたサブカテゴリーの家計簿記録のカテゴリーについて大カテゴリーを引き当て*/
    @Query("UPDATE balance SET balance_category_id = :categoryId WHERE balance_category_id = :balanceCategoryId")
    fun setDefaultBalanceCategoryId(categoryId: Int, balanceCategoryId: Int)

    /*category*/
    @get:Query("SELECT * FROM category ORDER BY category_id")
    val allCategory: List<Category>
}
