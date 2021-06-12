package com.non_name_hero.calenderview.calendar

import android.widget.GridView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.non_name_hero.calenderview.data.CalendarData

object ScheduleListBindings {
    /**
     * スケジュールのリストをAdapterに設定
     *
     * @param view         カレンダー
     * @param calendarData
     */
//    @BindingAdapter("app:schedules")
//    @kotlin.jvm.JvmStatic
//    fun setSchedules(view: GridView, calendarData: Map<String, List<CalendarData>>?) {
//        with(view.adapter as CalendarAdapter) {
//            if (calendarData != null) {
//                replaceData(calendarData)
//            }
//        }
//    }
//
//    @BindingAdapter("holidays")
//    @kotlin.jvm.JvmStatic
//    fun setHolidays(view: GridView, calendarData: Map<String, List<CalendarData>>?) {
//        if (calendarData != null) {
//            val adapter = view.adapter as CalendarAdapter
//            adapter.replaceHoliday(calendarData)
//        }
//    }

    @BindingAdapter("scheduleItems")
    @kotlin.jvm.JvmStatic
    fun setScheduleListItems(view: RecyclerView, calendarDataList: List<CalendarData>?) {
        with(view.adapter as ScheduleListAdapter) {
            if (calendarDataList != null) {
                if (this.calendarDataList != calendarDataList) {
                    setCalendarDataForScheduleList(calendarDataList)
                }
            }
        }
    }

//    @kotlin.jvm.JvmStatic
//    @InverseMethod("app:scheduleItems")
//    fun getScheduleListItems(view: RecyclerView): List<CalendarData> {
//        val adapter = view.adapter as ScheduleListAdapter
//        return adapter.calendarDataList.toMutableList()
//    }

//    @kotlin.jvm.JvmStatic
//    @BindingAdapter("app:scheduleItemsAttrChanged")
//    fun setListeners(view: RecyclerView, listener: InverseBindingListener) {
//        val adapter = view.adapter as ScheduleListAdapter
//        listener.onChange()
//    }
}