package com.non_name_hero.calenderview.calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.non_name_hero.calenderview.data.CalendarData;
import com.non_name_hero.calenderview.data.Schedule;
import com.non_name_hero.calenderview.data.source.ScheduleDataSource;
import com.non_name_hero.calenderview.data.source.ScheduleRepository;
import com.non_name_hero.calenderview.utils.PigLeadUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CalendarViewModel extends ViewModel implements ScheduleDataSource.GetScheduleCallback, ScheduleDataSource.LoadCalendarDataCallback, ScheduleDataSource.LoadHolidayCalendarDataCallback {
    // for calendar
    private final MutableLiveData<Map<String, List<Schedule>>> Schedules = new MutableLiveData<>();
    private final MutableLiveData<Map<String, List<CalendarData>>> mHolidayCalendarDataMap = new MutableLiveData<>();
    private final MutableLiveData<Map<String, List<CalendarData>>> mCalendarDataMap = new MutableLiveData<>();
    private final MutableLiveData<String> mCurrentMonth = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentMonth = new MutableLiveData<>();
    // for scheduleList
    private final MutableLiveData<Date> selectedDate = new MutableLiveData<>();
    public final MutableLiveData<List<CalendarData>> scheduleListData = new MutableLiveData<>();

    private ScheduleRepository mSchedulesRepository;

    public CalendarViewModel(ScheduleRepository SchedulesRepository) {
        this.mSchedulesRepository = SchedulesRepository;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        currentMonth.setValue(calendar.get(Calendar.MONTH) + 1);
        selectedDate.setValue(new Date());
    }

    public void start() {
        loadHolidaySchedules();
        reloadCalendarData(true);
    }

    public void reloadCalendarData(boolean forceUpdate) {
        if (forceUpdate) {
            mSchedulesRepository.calendarCacheClear();
        }
        mSchedulesRepository.getCalendarDataList(this);
    }

    private void loadHolidaySchedules() {
        //TODO firestoreでクラッシュする場合はコメントアウトする
        mSchedulesRepository.getHoliday(this);
    }

    // setters
    public void setCalendarDataMap(Map<String, List<CalendarData>> calendarDataMap) {
        mCalendarDataMap.setValue(calendarDataMap);
    }

    public void setCurrentMonth(Integer month) {
        currentMonth.setValue(month);
    }

    public void setHolidayCalendarDataMap(Map<String, List<CalendarData>> holidaySchedulesMap) {
        mHolidayCalendarDataMap.setValue(holidaySchedulesMap);
    }

    public LiveData<Map<String, List<Schedule>>> getSchedules() {
        return Schedules;
    }

    public LiveData<Map<String, List<CalendarData>>> getCalendarDataMap() {
        return mCalendarDataMap;
    }

    public LiveData<Map<String, List<CalendarData>>> getHolidayCalendarDataMap() {
        return mHolidayCalendarDataMap;
    }

    public LiveData<Integer> getCurrentMonth() {
        return currentMonth;
    }

    //callback
    @Override
    public void onScheduleLoaded(List<Schedule> schedules) {

    }

    @Override
    public void onCalendarDataLoaded(List<CalendarData> calendarDataList) {
        setCalendarDataMap(PigLeadUtils.getCalendarDataMapByCalendarDataList(calendarDataList));
        setScheduleItem(selectedDate.getValue());
    }

    @Override
    public void onHolidayCalendarDataLoaded(List<CalendarData> calendarDataList) {
        setHolidayCalendarDataMap(PigLeadUtils.getCalendarDataMapByCalendarDataList(calendarDataList));
    }

    @Override
    public void onDataNotAvailable() {

    }

    // etc
    public void setScheduleItem(Date date) {
        if (mCalendarDataMap != null) {
            String dateKey = PigLeadUtils.formatYYYYMMDD.format(date);
            scheduleListData.setValue(mCalendarDataMap.getValue().get(dateKey));
            selectedDate.setValue(date);
        }
    }

    public void saveScheduleItem() {

    }

    public void removeSchedule(long scheduleId) {
        mSchedulesRepository.removeScheduleByScheduleId(scheduleId);
        this.reloadCalendarData(true);
    }
}