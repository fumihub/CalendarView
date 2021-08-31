package com.non_name_hero.calenderview.utils

import com.non_name_hero.calenderview.data.BalanceData
import com.non_name_hero.calenderview.data.CalendarData
import com.non_name_hero.calenderview.data.Schedule
import java.text.SimpleDateFormat
import java.util.*

object PigLeadUtils {
    //年月日のフォーマット
    var formatYYYYMMDD = SimpleDateFormat("yyyy.MM.dd", Locale.US)
    var yearFormat = SimpleDateFormat("yyyy", Locale.US)
    var monthFormat = SimpleDateFormat("MM", Locale.US)
    var dayFormat = SimpleDateFormat("dd", Locale.US)
    var formatD = SimpleDateFormat("d", Locale.US)

    /**
     * List<Schedule> から Map<String></String>, List<Schedule>>へ変換する
     * @param schedules
     * @return scheduleMap -　Map<String></String>, List<Schedule>>
    </Schedule></Schedule></Schedule> */
    fun getScheduleMapBySchedules(schedules: List<Schedule>): Map<String, MutableList<Schedule>> {
        val scheduleMap: MutableMap<String, MutableList<Schedule>> = HashMap()
        var scheduleLists: MutableList<Schedule>
        for (schedule in schedules) {
            val date = formatYYYYMMDD.format(schedule.startAtDatetime)
            if (scheduleMap.containsKey(date)) {
                scheduleLists = scheduleMap[date]!!
                scheduleLists.add(schedule)
                scheduleLists = ArrayList(LinkedHashSet(scheduleLists))
            } else {
                scheduleLists = ArrayList()
                scheduleLists.add(schedule)
            }
            scheduleMap[date] = scheduleLists
        }
        return scheduleMap
    }

    /**
     * List<CalendarData> から Map<String></String>, List<CalendarData>>へ変換する
     * @param calendarDataList
     * @return scheduleMap -　Map<String></String>, List<Calendardata>>
    </Calendardata></CalendarData></CalendarData> */
    fun getCalendarDataMapByCalendarDataList(calendarDataList: List<CalendarData>): Map<String, MutableList<CalendarData>> {
        val calendarDataMap: MutableMap<String, MutableList<CalendarData>> = HashMap()
        var calendarDataLists: MutableList<CalendarData>
        for (data in calendarDataList) {
            // 日付キーを生成
            val date = formatYYYYMMDD.format(data.scheduleStartAtDatetime)
            if (calendarDataMap.containsKey(date)) {
                //calendarDataMap[date]が非nullであることは保証される
                calendarDataLists = calendarDataMap[date]!!
                calendarDataLists.add(data)
                // 重複するデータを削除してArrayListを再構成する
                calendarDataLists = ArrayList(LinkedHashSet(calendarDataLists))
            } else {
                calendarDataLists = ArrayList()
                calendarDataLists.add(data)
            }
            // 日付をキーとして値にデータのリストを格納
            calendarDataMap[date] = calendarDataLists
        }
        return calendarDataMap
    }
    /**
     * List<BalanceData> から Map<String></String>, List<CalendarData>>へ変換する
     * @param balanceDataList
     * @return scheduleMap 日付をキーとする家計簿データのHashMapを取得
     */
    fun getBalanceDataMapByBalanceDataList(balanceDataList: List<BalanceData>): Map<String, MutableList<BalanceData>> {
        val calendarBalanceDataMap: MutableMap<String, MutableList<BalanceData>> = HashMap()
        var balanceDataLists: MutableList<BalanceData>
        // 日付に家計簿データをマッピング
        for (data in balanceDataList) {
            // 日付キーを生成
            val date = formatYYYYMMDD.format(data.usedAtDatetime)
            if (calendarBalanceDataMap.containsKey(date)) {
                //balanceDataMap[date]が非nullであることは保証される
                balanceDataLists = calendarBalanceDataMap[date]!!
                balanceDataLists.add(data)
                // 重複するデータを削除してArrayListを再構成する
                balanceDataLists = ArrayList(LinkedHashSet(balanceDataLists))
            } else {
                balanceDataLists = ArrayList()
                balanceDataLists.add(data)
            }
            // 日付をキーとして値にデータのリストを格納
            calendarBalanceDataMap[date] = balanceDataLists
        }
        return calendarBalanceDataMap
    }
}