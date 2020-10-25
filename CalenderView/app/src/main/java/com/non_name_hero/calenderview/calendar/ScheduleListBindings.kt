package com.non_name_hero.calenderview.calendar

import android.widget.GridView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import com.non_name_hero.calenderview.data.CalendarData

object ScheduleListBindings {
    /**
     * スケジュールのリストをAdapterに設定
     *
     * @param view         カレンダー
     * @param calendarData
     */
    @kotlin.jvm.JvmStatic
    @BindingAdapter("schedules")
    fun setSchedules(view: GridView, calendarData: Map<String, List<CalendarData>>) {
        val adapter = view.adapter as CalendarAdapter
        adapter?.replaceData(calendarData)
    }

    @kotlin.jvm.JvmStatic
    @BindingAdapter("holidays")
    fun setHolidays(view: GridView, calendarData: Map<String, List<CalendarData>>) {
        val adapter = view.adapter as CalendarAdapter
        adapter?.replaceHoliday(calendarData)
    }

    @kotlin.jvm.JvmStatic
    @BindingAdapter("scheduleItems")
    fun setScheduleListItems(view: RecyclerView, calendarDataList: List<CalendarData>) {
        val adapter = view.adapter as ScheduleListAdapter?
        if (adapter != null) {
            if (adapter.calendarDataList !== calendarDataList) {
                adapter.setCalendarDataForScheduleList(calendarDataList)
            }
        }
    }

    @kotlin.jvm.JvmStatic
    @InverseBindingAdapter(attribute = "scheduleItems", event = "scheduleItemsAttrChanged")
    fun getScheduleListItems(view: RecyclerView): List<CalendarData?>? {
        val adapter = view.adapter as ScheduleListAdapter?
        return adapter!!.calendarDataList
    }

    @kotlin.jvm.JvmStatic
    @BindingAdapter("app:scheduleItemsAttrChanged")
    fun setListeners(view: RecyclerView, listener: InverseBindingListener) {
        val adapter = view.adapter as ScheduleListAdapter?
        listener.onChange()
    }
}