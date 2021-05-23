package com.non_name_hero.calenderview.data.source

import com.non_name_hero.calenderview.data.*

interface ScheduleDataSource {
    interface GetScheduleCallback {
        fun onScheduleLoaded(schedules: List<Schedule>)
        fun onDataNotAvailable()
    }

    interface GetScheduleMapCallback {
        fun onScheduleMapLoaded(scheduleStringMap: Map<String, List<Schedule>>)
    }

    interface SaveScheduleCallback {
        fun onScheduleSaved()
        fun onDataNotSaved()
    }

    fun getSchedule(ScheduleIds: LongArray, callback: GetScheduleCallback)
    fun setSchedule(schedule: Schedule, callback: SaveScheduleCallback)
    fun getAllSchedules(callback: GetScheduleCallback)
    fun getAllBalances(callback: GetBalanceCallback)
    fun getAllBalanceCategories(callback: GetBalanceCategoriesCallback)
    fun removeScheduleByScheduleId(scheduleId: Long)

    /**
     * ScheduleGroupのコールバック
     * onGroupSaved() -　保存成功時の処理
     * onDataNotSaved() - 保存失敗時の処理
     */
    interface SaveScheduleGroupCallback {
        fun onScheduleGroupSaved()
        fun onDataNotSaved()
    }

    //複数件取得時のコールバック
    interface GetScheduleGroupsCallback {
        fun onScheduleGroupsLoaded(Groups: List<ScheduleGroup>)
        fun onDataNotAvailable()
    }

    //1件取得時コールバック
    interface GetScheduleGroupCallback {
        fun onScheduleGroupLoaded(group: ScheduleGroup)
    }

    //複数件取得時のコールバック
    interface GetBalanceCallback {
        fun onBalanceLoaded(Groups: List<Balance>)
        fun onDataNotAvailable()
    }

    //複数件取得時のコールバック
    interface GetBalanceCategoriesCallback {
        fun onBalanceCategoryLoaded(Groups: List<BalanceCategory>)
        fun onDataNotAvailable()
    }

    //1件取得時コールバック
    interface GetBalanceCategoryCallback {
        fun onBalanceCategoryLoaded(group: BalanceCategory)
    }

    // 削除時のコールバック
    interface DeleteCallback {
        fun onDeleted()
        fun onDataNotDeleted()
    }

    fun insertScheduleGroup(group: ScheduleGroup, callback: SaveScheduleGroupCallback)
    fun deleteScheduleGroup(groupId: Int, callback: DeleteCallback)
    fun getScheduleGroup(colorNumber: Int, callback: GetScheduleGroupCallback)
    fun getListScheduleGroup(callback: GetScheduleGroupsCallback)
    fun updateScheduleGroup(group: ScheduleGroup, callback: SaveScheduleGroupCallback)
    interface LoadHolidayCalendarDataCallback {
        fun onHolidayCalendarDataLoaded(calendarDataList: List<CalendarData>)
        fun onDataNotAvailable()
    }

    interface LoadCalendarDataCallback {
        fun onCalendarDataLoaded(calendarDataList: List<CalendarData>)
        fun onDataNotAvailable()
    }

    fun getHoliday(callback: LoadHolidayCalendarDataCallback)
    fun getCalendarDataList(callback: LoadCalendarDataCallback)
}