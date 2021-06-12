package com.non_name_hero.calenderview.calendar

import android.widget.GridView
import android.widget.ListView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.non_name_hero.calenderview.data.CalendarData
import com.non_name_hero.calenderview.inputForm.ListAdapter
import com.non_name_hero.calenderview.inputForm.SubCategoryListAdapter
import com.non_name_hero.calenderview.inputForm.SubCategorySelectActivity

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

    @BindingAdapter("colorEditMode")
    @kotlin.jvm.JvmStatic
    fun setColorEditMode(view: ListView, editMode: LiveData<Boolean>) {
        with(view.adapter as ListAdapter) {
            this.colorEditMode = editMode.value?:false
        }
    }

    @BindingAdapter("editMode")
    @kotlin.jvm.JvmStatic
    fun setEditMode(view: ListView, editMode: LiveData<Boolean>) {
        with(view.adapter as SubCategoryListAdapter) {
            this.editMode = editMode.value?:false
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