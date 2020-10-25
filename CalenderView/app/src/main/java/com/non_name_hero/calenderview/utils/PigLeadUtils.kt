package com.non_name_hero.calenderview.utils

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
    fun getCalendarDataMapByCalendarDataList(calendarDataList: List<CalendarData?>?): Map<String, MutableList<CalendarData?>> {
        val calendarDataMap: MutableMap<String, MutableList<CalendarData?>> = HashMap()
        var calendarDataLists: MutableList<CalendarData?>
        for (data in calendarDataList!!) {
            val date = formatYYYYMMDD.format(data!!.scheduleStartAtDatetime)
            if (calendarDataMap.containsKey(date)) {
                calendarDataLists = calendarDataMap[date]!!
                calendarDataLists.add(data)
                calendarDataLists = ArrayList(LinkedHashSet(calendarDataLists))
            } else {
                calendarDataLists = ArrayList()
                calendarDataLists.add(data)
            }
            calendarDataMap[date] = calendarDataLists
        }
        return calendarDataMap
    }
}