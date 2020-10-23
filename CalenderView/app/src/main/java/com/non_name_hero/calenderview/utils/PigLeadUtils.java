package com.non_name_hero.calenderview.utils;

import com.non_name_hero.calenderview.data.CalendarData;
import com.non_name_hero.calenderview.data.Schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PigLeadUtils {
    //年月日のフォーマット
    public static SimpleDateFormat formatYYYYMMDD = new SimpleDateFormat("yyyy.MM.dd", Locale.US);
    public static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.US);
    public static SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.US);
    public static SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.US);
    public static SimpleDateFormat formatD = new SimpleDateFormat("d", Locale.US);

    /**
     * List<Schedule> から Map<String, List<Schedule>>へ変換する
     * @param schedules
     * @return scheduleMap -　Map<String, List<Schedule>>
     */
    public static Map<String, List<Schedule>> getScheduleMapBySchedules(List<Schedule> schedules){
        Map<String, List<Schedule>> scheduleMap= new HashMap<>();
        List<Schedule> scheduleLists;
        for (Schedule schedule : schedules) {
            String date = formatYYYYMMDD.format(schedule.getStartAtDatetime());
            if (scheduleMap.containsKey(date)) {
                scheduleLists = scheduleMap.get(date);
                scheduleLists.add(schedule);
                scheduleLists = new ArrayList<>(new LinkedHashSet<>(scheduleLists));

            } else {
                scheduleLists = new ArrayList<Schedule>();
                scheduleLists.add(schedule);
            }
            scheduleMap.put(date, scheduleLists);
        }
        return scheduleMap;
    }

    /**
     * List<CalendarData> から Map<String, List<CalendarData>>へ変換する
     * @param calendarDataList
     * @return scheduleMap -　Map<String, List<Calendardata>>
     */
    public static Map<String, List<CalendarData>> getCalendarDataMapByCalendarDataList(List<CalendarData> calendarDataList){
        Map<String, List<CalendarData>> calendarDataMap= new HashMap<>();
        List<CalendarData> calendarDataLists;
        for (CalendarData data : calendarDataList) {
            String date = formatYYYYMMDD.format(data.scheduleStartAtDatetime);
            if (calendarDataMap.containsKey(date)) {
                calendarDataLists = calendarDataMap.get(date);
                calendarDataLists.add(data);
                calendarDataLists = new ArrayList<>(new LinkedHashSet<>(calendarDataLists));
            } else {
                calendarDataLists = new ArrayList<CalendarData>();
                calendarDataLists.add(data);
            }
            calendarDataMap.put(date, calendarDataLists);
        }
        return calendarDataMap;
    }

}
