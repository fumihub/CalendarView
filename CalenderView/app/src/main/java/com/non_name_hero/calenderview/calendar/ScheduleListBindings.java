package com.non_name_hero.calenderview.calendar;

import android.widget.GridView;

import androidx.databinding.BindingAdapter;

import com.non_name_hero.calenderview.data.Schedule;

import java.util.List;
import java.util.Map;

public class ScheduleListBindings {
    /**
     * スケジュールのリストをAdapterに設定
     *
     * @param view カレンダー
     * @param scheduleMaps
     */
    @SuppressWarnings("unchecked")
    @BindingAdapter({"schedules"})
    public static void setSchedules(GridView view, Map<String, List<Schedule>> scheduleMaps) {
        CalendarAdapter adapter = (CalendarAdapter) view.getAdapter();
        if (adapter != null) {
            adapter.replaceData(scheduleMaps);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"holidays"})
    public static void setHolidays(GridView view, Map<String, List<Schedule>> scheduleMaps) {
        CalendarAdapter adapter = (CalendarAdapter) view.getAdapter();
        if (adapter != null) {
            adapter.replaceHoliday(scheduleMaps);
        }
    }
}
