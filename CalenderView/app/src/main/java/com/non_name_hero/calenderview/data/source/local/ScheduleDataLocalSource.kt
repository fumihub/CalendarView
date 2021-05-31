package com.non_name_hero.calenderview.data.source.local

import com.non_name_hero.calenderview.data.Schedule
import com.non_name_hero.calenderview.data.ScheduleGroup
import com.non_name_hero.calenderview.data.source.ScheduleDataSource
import com.non_name_hero.calenderview.data.source.ScheduleDataSource.*
import com.non_name_hero.calenderview.utils.AppExecutors

class ScheduleDataLocalSource  //コンストラクタ
(val appExecutors: AppExecutors,
 val schedulesDao: SchedulesDao) : ScheduleDataSource {
    /**
     * スケジュールIDの配列からscheduleオブジェクト配列を取得
     *
     * @param scheduleIds
     * @param callback
     */
    override fun getAllBalances(callback: GetBalanceCallback) {
        val runnable = Runnable {
            val schedules = schedulesDao.allBalance
            appExecutors.mainThread.execute { callback.onBalanceLoaded(schedules) }
        }
        appExecutors.diskIO.execute(runnable)
    }

    override fun getSchedule(scheduleIds: LongArray, callback: GetScheduleCallback) {
        val runnable = Runnable {
            val schedules = schedulesDao.loadSchedulesByIds(scheduleIds)
            appExecutors.mainThread.execute { callback.onScheduleLoaded(schedules) }
        }
        appExecutors.diskIO.execute(runnable)
    }

    override fun setSchedule(schedule: Schedule, callback: SaveScheduleCallback) {
        val runnable = Runnable {
            schedulesDao.insertSchedule(schedule)
            //insert完了
            appExecutors.mainThread.execute { callback.onScheduleSaved() }
        }
        appExecutors.diskIO.execute(runnable)
    }

    override fun getAllSchedules(callback: GetScheduleCallback) {
        val runnable = Runnable {
            val schedules = schedulesDao.allSchedules
            appExecutors.mainThread.execute { callback.onScheduleLoaded(schedules) }
        }
        appExecutors.diskIO.execute(runnable)
    }

    override fun removeScheduleByScheduleId(scheduleId: Long) {
        val runnable = Runnable { schedulesDao.deleteByScheduleId(scheduleId) }
        appExecutors.diskIO.execute(runnable)
    }

    override fun insertScheduleGroup(group: ScheduleGroup, callback: SaveScheduleGroupCallback) {
        val runnable = Runnable {
            schedulesDao.insertScheduleGroup(group)
            appExecutors.mainThread.execute { callback.onScheduleGroupSaved() }
        }
        appExecutors.diskIO.execute(runnable)
    }

    override fun deleteScheduleGroup(groupId: Int, callback: DeleteCallback) {
        val runnable = Runnable {
            schedulesDao.deleteScheduleGroupByColorNumber(groupId)
            schedulesDao.setDefaultGroupId(groupId)
            appExecutors.mainThread.execute { callback.onDeleted() }
        }
        appExecutors.diskIO.execute(runnable)
    }

    override fun getScheduleGroup(colorNumber: Int, callback: GetScheduleGroupCallback) {
        val runnable = Runnable {
            val group = schedulesDao.getScheduleGroupByColorNumber(colorNumber)[0]
            appExecutors.mainThread.execute { callback.onScheduleGroupLoaded(group) }
        }
        appExecutors.diskIO.execute(runnable)
    }

    override fun getListScheduleGroup(callback: GetScheduleGroupsCallback) {
        val runnable = Runnable {
            val groups = schedulesDao.allScheduleGroup
            appExecutors.mainThread.execute { callback.onScheduleGroupsLoaded(groups) }
        }
        appExecutors.diskIO.execute(runnable)
    }

    override fun updateScheduleGroup(group: ScheduleGroup, callback: SaveScheduleGroupCallback) {
        val runnable = Runnable {
            schedulesDao.updateScheduleGroup(group)
            appExecutors.mainThread.execute { callback.onScheduleGroupSaved() }
        }
        appExecutors.diskIO.execute(runnable)
    }

    override fun getHoliday(callback: LoadHolidayCalendarDataCallback) {}
    override fun getCalendarDataList(callback: LoadCalendarDataCallback) {
        val runnable = Runnable {
            val calendarDataList = schedulesDao.allCalendarDataList
            appExecutors.mainThread.execute { callback.onCalendarDataLoaded(calendarDataList) }
        }
        appExecutors.diskIO.execute(runnable)
    }

    companion object {
        @Volatile
        private var INSTANCE: ScheduleDataLocalSource? = null

        //シングルトン
        @JvmStatic
        fun getInstance(appExecutors: AppExecutors,
                        usersDao: SchedulesDao): ScheduleDataLocalSource? {
            if (INSTANCE == null) {
                synchronized(ScheduleDataLocalSource::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = ScheduleDataLocalSource(appExecutors, usersDao)
                    }
                }
            }
            return INSTANCE
        }
    }

    override fun getCategoriesData(categoryId: Int, callback: GetCategoriesDataCallback) {
        val runnable = Runnable {
            val categoryData = schedulesDao.getCategoryDataList(categoryId)
            appExecutors.mainThread.execute { callback.onCategoriesDataLoaded(categoryData) }
        }
        appExecutors.diskIO.execute(runnable)
    }

    override fun getCategoryData(balanceCategoryId: Int, callback: GetCategoryDataCallback) {
         val runnable = Runnable {
            val categoryData = schedulesDao.getCategoryDataByBalanceCategoryId(balanceCategoryId)[0]
            appExecutors.mainThread.execute { callback.onCategoryDataLoaded(categoryData) }
        }
        appExecutors.diskIO.execute(runnable)
    }

    override fun getCategory(callback: GetCategoryCallback){
        val runnable = Runnable {
            val category = schedulesDao.allCategory
            appExecutors.mainThread.execute { callback.onCategoryLoaded(category) }
        }
        appExecutors.diskIO.execute(runnable)
    }
}