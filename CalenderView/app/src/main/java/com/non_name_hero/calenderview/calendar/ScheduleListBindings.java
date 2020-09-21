package com.non_name_hero.calenderview.calendar;

import android.widget.GridView;

import androidx.databinding.BindingAdapter;

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
}
