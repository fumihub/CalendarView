package com.non_name_hero.calenderview.data.source.local

import com.non_name_hero.calenderview.data.Schedule
import com.non_name_hero.calenderview.data.ScheduleGroup
import com.non_name_hero.calenderview.data.source.ScheduleDataSource
import com.non_name_hero.calenderview.data.source.ScheduleDataSource.*
import com.non_name_hero.calenderview.utils.AppExecutors

class ScheduleDataLocalSource  //コンストラクタ
private constructor(private val mAppExecutors: AppExecutors,
                    private val mSchedulesDao: SchedulesDao) : ScheduleDataSource {
    /**
     * スケジュールIDの配列からscheduleオブジェクト配列を取得
     *
     * @param scheduleIds
     * @param callback
     */
    override fun getSchedule(scheduleIds: LongArray, callback: GetScheduleCallback) {
        val runnable = Runnable {
            val schedules = mSchedulesDao.loadSchedulesByIds(scheduleIds)
            mAppExecutors.mainThread().execute { callback.onScheduleLoaded(schedules) }
        }
        mAppExecutors.diskIO().execute(runnable)
    }

    override fun setSchedule(schedule: Schedule?, callback: SaveScheduleCallback) {
        val runnable = Runnable {
            mSchedulesDao.insertSchedule(schedule)
            //insert完了
            mAppExecutors.mainThread().execute { callback.onScheduleSaved() }
        }
        mAppExecutors.diskIO().execute(runnable)
    }

    override fun getAllSchedules(callback: GetScheduleCallback) {
        val runnable = Runnable {
            val schedules = mSchedulesDao.all
            mAppExecutors.mainThread().execute { callback.onScheduleLoaded(schedules) }
        }
        mAppExecutors.diskIO().execute(runnable)
    }

    override fun removeScheduleByScheduleId(scheduleId: Long) {
        val runnable = Runnable { mSchedulesDao.deleteByScheduleId(scheduleId) }
        mAppExecutors.diskIO().execute(runnable)
    }

    override fun insertScheduleGroup(group: ScheduleGroup, callback: SaveScheduleGroupCallback) {
        val runnable = Runnable {
            mSchedulesDao.insertScheduleGroup(group)
            mAppExecutors.mainThread().execute { callback.onScheduleGroupSaved() }
        }
        mAppExecutors.diskIO().execute(runnable)
    }

    override fun deleteScheduleGroup(groupId: Int, callback: DeleteCallback) {
        val runnable = Runnable {
            mSchedulesDao.deleteScheduleGroupByColorNumber(groupId)
            mSchedulesDao.setDefaultGroupId(groupId)
            mAppExecutors.mainThread().execute { callback.onDeleted() }
        }
        mAppExecutors.diskIO().execute(runnable)
    }

    override fun getScheduleGroup(colorNumber: Int, callback: GetScheduleGroupCallback) {
        val runnable = Runnable {
            val group = mSchedulesDao.getScheduleGroupByColorNumber(colorNumber)[0]
            mAppExecutors.mainThread().execute { callback.onScheduleGroupLoaded(group) }
        }
        mAppExecutors.diskIO().execute(runnable)
    }

    override fun getListScheduleGroup(callback: GetScheduleGroupsCallback) {
        val runnable = Runnable {
            val groups = mSchedulesDao.allScheduleGroup
            mAppExecutors.mainThread().execute { callback.onScheduleGroupsLoaded(groups) }
        }
        mAppExecutors.diskIO().execute(runnable)
    }

    override fun updateScheduleGroup(group: ScheduleGroup, callback: SaveScheduleGroupCallback) {
        val runnable = Runnable {
            mSchedulesDao.updateScheduleGroup(group)
            mAppExecutors.mainThread().execute { callback.onScheduleGroupSaved() }
        }
        mAppExecutors.diskIO().execute(runnable)
    }

    override fun getHoliday(callback: LoadHolidayCalendarDataCallback) {}
    override fun getCalendarDataList(callback: LoadCalendarDataCallback) {
        val runnable = Runnable {
            val calendarDataList = mSchedulesDao.allCalendarDataList
            mAppExecutors.mainThread().execute { callback.onCalendarDataLoaded(calendarDataList) }
        }
        mAppExecutors.diskIO().execute(runnable)
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
}