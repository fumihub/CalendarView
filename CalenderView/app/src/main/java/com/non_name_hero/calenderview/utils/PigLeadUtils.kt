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
                scheduleLists = mutableListOf(schedule)
            }
            scheduleMap[date] = scheduleLists
        }
        return scheduleMap
    }

    /**
     * List<CalendarData> から Map<String>, List<CalendarData>>へ変換する
     * @param calendarDataList
     * @return scheduleMap<Calendardata>
     **/
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
                calendarDataLists = mutableListOf(data)
            }
            // 日付をキーとして値にデータのリストを格納
            calendarDataMap[date] = calendarDataLists
        }
        return calendarDataMap
    }

    fun getBalanceCalendarDataMapByBalanceDataList(balanceDataList: List<BalanceData>): Map<String, MutableList<BalanceData>> {
        val calendarBalanceDataMap: MutableMap<String, MutableList<BalanceData>> = HashMap()
        var balanceDataLists: MutableList<BalanceData>
        // 日付に家計簿データをマッピング
        for (data in balanceDataList) {
            // 日付キーを生成
            val date = data.timestamp
            if (calendarBalanceDataMap.containsKey(date)) {
                // すでにその日にちのデータがある場合は値を取得した上で追加
                balanceDataLists = calendarBalanceDataMap[date]!!
                // 同じbalanceType(収支タイプ)が同じ要素がなければ要素を追加
                balanceDataLists.add(data)
            } else {
                // すでにその日にちのデータがない場合は新規にArrayList作成
                balanceDataLists = mutableListOf(data)
            }
            // 日付をキーとして値にデータのリストを格納
            calendarBalanceDataMap[date] = balanceDataLists
        }
        return calendarBalanceDataMap
    }
}