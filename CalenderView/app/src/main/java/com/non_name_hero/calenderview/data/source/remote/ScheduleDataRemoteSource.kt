package com.non_name_hero.calenderview.data.source.remote

import android.content.ContentValues
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.non_name_hero.calenderview.data.BalanceCategory
import com.non_name_hero.calenderview.data.CalendarData
import com.non_name_hero.calenderview.data.Schedule
import com.non_name_hero.calenderview.data.ScheduleGroup
import com.non_name_hero.calenderview.data.source.ScheduleDataSource
import com.non_name_hero.calenderview.data.source.ScheduleDataSource.*
import com.non_name_hero.calenderview.utils.AppExecutors
import java.util.*

class ScheduleDataRemoteSource() : ScheduleDataSource {
    private val PIGLEAD_SCHEDULES = "PigLeadSchedules"
    private val HOLIDAY_DOCUMENT = "holiday"

    //リモートデータベース
    private val db: FirebaseFirestore
    override fun getSchedule(ScheduleIds: LongArray, callback: GetScheduleCallback) {
        //リモートデータソースは使用しない
    }

    override fun setSchedule(schedule: Schedule, callback: SaveScheduleCallback) {
        //リモートデータソースは使用しない
    }

    override fun getAllSchedules(callback: GetScheduleCallback) {
        //リモートデータソースは使用しない
    }

    override fun getAllBalances(callback: GetBalanceCallback) {
        //リモートデータソースは使用しない
    }

    override fun removeScheduleByScheduleId(scheduleId: Long) {}
    override fun getHoliday(callback: LoadHolidayCalendarDataCallback) {
        //以下は別スレッドにて実行
        db.collection(PIGLEAD_SCHEDULES)
                .document(HOLIDAY_DOCUMENT)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        //documentにドキュメントデータがあればtrue
                        if (document!!.exists()) {
                            val holiday = document.data //documentからholidayNameのバリューを取得
                            //受け取ったデータを整形
                            val holidaySchedules: MutableList<CalendarData> = ArrayList()
                            for (obj in holiday!!.values) {
                                val holidayData = autoCast<Map<String, Any>>(obj)!!
                                val date = (holidayData["date"] as Timestamp?)!!.seconds * 1000
                                val data = CalendarData()
                                data.scheduleTitle = holidayData["nameInJapan"] as String?
                                data.scheduleStartAtDatetime = Date(date)
                                data.isHoliday = true
                                holidaySchedules.add(data)
                            }
                            //callbackに引数を渡す(データ配列)
                            callback.onHolidayCalendarDataLoaded(holidaySchedules)
                            Log.d(ContentValues.TAG, holiday.toString())
                        } else {
                            Log.d(ContentValues.TAG, "No such document")
                        }
                    } else {
                        Log.d(ContentValues.TAG, "failure task firebase")
                    }
                    Log.d(ContentValues.TAG, "get failed with ", task.exception)
                }
    }

    override fun insertScheduleGroup(group: ScheduleGroup, callback: SaveScheduleGroupCallback) {}
    override fun deleteScheduleGroup(groupId: Int, callback: DeleteCallback) {
        //リモートでは処理しない
    }

    override fun getScheduleGroup(colorNumber: Int, callback: GetScheduleGroupCallback) {}
    override fun getListScheduleGroup(callback: GetScheduleGroupsCallback) {}
    override fun updateScheduleGroup(group: ScheduleGroup, callback: SaveScheduleGroupCallback) {}
    override fun getCalendarDataList(callback: LoadCalendarDataCallback) {}
    override fun getCategoriesData(categoryId: Int, callback: GetCategoriesDataCallback) {}
    override fun getCategoryData(categoryId: Int, callback: GetCategoryDataCallback) {}

    override fun getCategory(callback: GetCategoryCallback) {}
    override fun insertBalanceCategory(balanceCategory: BalanceCategory, callback: SaveBalanceCategoryCallback) {}

    override fun deleteBalanceCategory(balanceCategoryId: Int, callback: DeleteCallback) {}

    fun <T> autoCast(obj: Any?): T? {
        return obj as T?
    }

    companion object {
        @Volatile
        private var INSTANCE: ScheduleDataRemoteSource? = null
        @JvmStatic
        fun getInstance(appExecutors: AppExecutors): ScheduleDataRemoteSource? {
            if (INSTANCE == null) {
                synchronized(ScheduleDataRemoteSource::class.java) { INSTANCE = ScheduleDataRemoteSource() }
            }
            return INSTANCE
        }
    }

    init {
        db = FirebaseFirestore.getInstance()
    }
}