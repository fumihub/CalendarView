package com.non_name_hero.calenderview.data.source.local

import androidx.lifecycle.LiveData
import com.non_name_hero.calenderview.data.*
import com.non_name_hero.calenderview.data.source.ScheduleDataSource
import com.non_name_hero.calenderview.data.source.ScheduleDataSource.*
import com.non_name_hero.calenderview.utils.AppExecutors
import java.text.SimpleDateFormat
import java.util.*

class ScheduleDataLocalSource  //コンストラクタ
(
        val appExecutors: AppExecutors,
        val schedulesDao: SchedulesDao
) : ScheduleDataSource {

    /*UserInfo*/
    override fun getUserInfo(mailAdress: String, callback: GetUserInfoCallback) {}

    override fun setUserInfo(mailAdress: String, password: String, callback: SaveUserInfoCallback) {}

    override fun changeUserInfo(mailAddress: String, newPassword: String, callback: ChangeUserInfoCallback) {}

    /*Schedule*/
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

    override fun pickUpSchedules(
            targetStartDate: Date,
            targetEndDate: Date,
            callback: PickUpScheduleCallback
    ) {
        val runnable = Runnable {
            val schedules = schedulesDao.pickUpSchedules(targetStartDate, targetEndDate)
            appExecutors.mainThread.execute { callback.onScheduleLoaded(schedules) }
        }
        appExecutors.diskIO.execute(runnable)
    }


    /*ScheduleGroup*/
    override fun insertScheduleGroup(colorNumber: Int, colorCreateTitle: String, textColor: String, color: Int, callback: SaveScheduleGroupCallback) {
        val runnable = Runnable {
            val primaryKey = schedulesDao.insertScheduleGroup(colorNumber, colorCreateTitle, textColor, color)
            appExecutors.mainThread.execute { callback.onScheduleGroupSaved(primaryKey) }
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

    override fun updateScheduleGroup(groupId: Int, colorNumber: Int, colorCreateTitle: String, textColor: String, color: Int, callback: UpdateScheduleGroupCallback) {
        val runnable = Runnable {
            val updateLine = schedulesDao.updateScheduleGroup(groupId, colorNumber, colorCreateTitle, textColor, color)
            appExecutors.mainThread.execute { callback.onScheduleGroupSaved(updateLine) }
        }
        appExecutors.diskIO.execute(runnable)
    }


    /*CalendarData*/
    override fun getHoliday(callback: LoadHolidayCalendarDataCallback) {}

    override fun getCalendarDataList(callback: LoadCalendarDataCallback) {
        val runnable = Runnable {
            val calendarDataList = schedulesDao.allCalendarDataList
            appExecutors.mainThread.execute { callback.onCalendarDataLoaded(calendarDataList) }
        }
        appExecutors.diskIO.execute(runnable)
    }


    /*Balance*/
    /**
     * balanceIDの配列からbalanceオブジェクト配列を取得
     *
     * @param balanceIds
     * @param callback
     */
    override fun getAllBalances(callback: GetBalanceCallback) {
        val runnable = Runnable {
            val balances = schedulesDao.allBalance
            appExecutors.mainThread.execute { callback.onBalanceLoaded(balances) }
        }
        appExecutors.diskIO.execute(runnable)
    }

    /**
     * balanceDataを取得しcallbackを実行
     *
     * @param startMonth 取得範囲の開始月
     * @param endMonth 取得範囲の終了月
     */
    override fun getBalanceData(
            startMonth: Date?,
            endMonth: Date?,
            callback: GetBalanceDataCallback
    ) {
        val runnable = Runnable {
            val balanceDataList = if (startMonth != null && endMonth != null) {
                schedulesDao.getBalanceDataListByMonthPeriod(startMonth, endMonth)
            } else {
                schedulesDao.getBalanceDataList()
            }
            appExecutors.mainThread.execute { callback.onBalanceDataLoaded(balanceDataList) }
        }
        appExecutors.diskIO.execute(runnable)

    }

    /**
     * balanceCategoryDataを取得しcallbackを実行
     *
     * @param startMonth 取得範囲の開始月
     * @param endMonth 取得範囲の終了月
     */
    override fun getBalanceCategoryData(startMonth: Date, endMonth: Date, callback: GetBalanceCategoryDataCallback) {
        val runnable = Runnable {
            val balanceCategoryDataList = schedulesDao.getBalanceCategoryDataListByMonthPeriod(startMonth, endMonth)
            appExecutors.mainThread.execute { callback.onBalanceCategoryDataLoaded(balanceCategoryDataList) }
        }
    }

    /**
     * 家計簿のサマリーを取得
     * @param yearMonth 期間を指定。nullで全期間の集計
     * @param callback 取得完了後の処理
     */
    override fun getBalanceSummary(yearMonth: Date?, callback: GetBalanceSummaryCallback) {
        val runnable = Runnable {
            val balanceSummary: List<BalanceData> = if (yearMonth !== null) {
                val sdFormat = SimpleDateFormat("yyyy.MM")
                val dateStr = sdFormat.format(yearMonth)
                schedulesDao.getBalanceSummary(dateStr)
            } else {
                schedulesDao.getBalanceSummary()
            }
            if (balanceSummary.isNotEmpty()) {
                appExecutors.mainThread.execute { callback.onBalanceDataLoaded(balanceSummary) }
            } else {
                appExecutors.mainThread.execute { callback.onDataNotAvailable() }
            }
        }
        appExecutors.diskIO.execute(runnable)
    }

    override fun insertBalance(balance: Balance, callback: SaveBalanceCallback) {
        val runnable = Runnable {
            schedulesDao.insertBalance(balance)
            appExecutors.mainThread.execute { callback.onBalanceSaved() }
        }
        appExecutors.diskIO.execute(runnable)
    }

    override fun removeBalanceByBalanceId(balanceId: Long) {
        val runnable = Runnable { schedulesDao.deleteByBalanceId(balanceId) }
        appExecutors.diskIO.execute(runnable)
    }


    /*CategoryData*/
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


    /*Category*/
    override fun getCategory(callback: GetCategoryCallback) {
        val runnable = Runnable {
            val category = schedulesDao.allCategory
            appExecutors.mainThread.execute { callback.onCategoryLoaded(category) }
        }
        appExecutors.diskIO.execute(runnable)
    }


    /*BalanceCategory*/
    override fun insertBalanceCategory(editFlag: Boolean, balanceCategoryName: String, categoryId: Int, callback: SaveBalanceCategoryCallback) {
        val runnable = Runnable {
            val primaryKey = schedulesDao.insertBalanceCategory(editFlag, balanceCategoryName, categoryId)
            appExecutors.mainThread.execute { callback.onBalanceCategorySaved(primaryKey) }
        }
        appExecutors.diskIO.execute(runnable)
    }

    override fun deleteBalanceCategory(
            categoryId: Int,
            balanceCategoryId: Int,
            callback: DeleteCallback
    ) {
        val runnable = Runnable {
            schedulesDao.deleteBalanceCategoryByBalanceCategoryId(balanceCategoryId)
            schedulesDao.setDefaultBalanceCategoryId(categoryId, balanceCategoryId)
            appExecutors.mainThread.execute { callback.onDeleted() }
        }
        appExecutors.diskIO.execute(runnable)
    }


    /*singleTon*/
    companion object {
        @Volatile
        private var INSTANCE: ScheduleDataLocalSource? = null

        @JvmStatic
        fun getInstance(
                appExecutors: AppExecutors,
                usersDao: SchedulesDao
        ): ScheduleDataLocalSource? {
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