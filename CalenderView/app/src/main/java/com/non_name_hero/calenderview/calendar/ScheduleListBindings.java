package com.non_name_hero.calenderview.calendar;

import android.widget.GridView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.recyclerview.widget.RecyclerView;

import com.non_name_hero.calenderview.data.CalendarData;

import java.util.List;
import java.util.Map;

public class ScheduleListBindings {
    /**
     * スケジュールのリストをAdapterに設定
     *
     * @param view         カレンダー
     * @param calendarData
     */
    @SuppressWarnings("unchecked")
    @BindingAdapter({"schedules"})
    public static void setSchedules(GridView view, Map<String, List<CalendarData>> calendarData) {
        CalendarAdapter adapter = (CalendarAdapter) view.getAdapter();
        if (adapter != null) {
            adapter.replaceData(calendarData);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"holidays"})
    public static void setHolidays(GridView view, Map<String, List<CalendarData>> calendarData) {
        CalendarAdapter adapter = (CalendarAdapter) view.getAdapter();
        if (adapter != null) {
            adapter.replaceHoliday(calendarData);
        }
    }

    @BindingAdapter("scheduleItems")
    public static void setScheduleListItems(RecyclerView view, List<CalendarData> calendarDataList) {
        ScheduleListAdapter adapter = (ScheduleListAdapter) view.getAdapter();
        if (adapter != null) {
            if (adapter.calendarDataList != calendarDataList) {
                adapter.setCalendarDataForScheduleList(calendarDataList);
            }
        }
    }

    @InverseBindingAdapter(attribute = "scheduleItems", event = "scheduleItemsAttrChanged")
    public static List<CalendarData> getScheduleListItems(RecyclerView view) {
        ScheduleListAdapter adapter = (ScheduleListAdapter) view.getAdapter();
        return adapter.calendarDataList;
    }

    @BindingAdapter("app:scheduleItemsAttrChanged")
    public static void setListeners(RecyclerView view, final InverseBindingListener listener) {
        ScheduleListAdapter adapter = (ScheduleListAdapter) view.getAdapter();
        listener.onChange();
    }
}
