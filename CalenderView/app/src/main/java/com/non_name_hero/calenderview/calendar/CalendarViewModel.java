package com.non_name_hero.calenderview.calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.non_name_hero.calenderview.data.Schedule;
import com.non_name_hero.calenderview.data.source.ScheduleDataSource;
import com.non_name_hero.calenderview.data.source.ScheduleRepository;
import com.non_name_hero.calenderview.utils.PigLeadUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class CalendarViewModel extends ViewModel implements ScheduleDataSource.GetScheduleMapCallback, ScheduleDataSource.GetScheduleCallback {

    private final MutableLiveData<Map<String, List<Schedule>>> Schedules = new MutableLiveData<>();
    private final MutableLiveData<Map<String, List<Schedule>>> mHolidaySchedules = new MutableLiveData<>();
    private final MutableLiveData<String> mCurrentMonth = new MutableLiveData<>();
    private ScheduleRepository mSchedulesRepository;

    public CalendarViewModel(ScheduleRepository SchedulesRepository) {
        this.mSchedulesRepository = SchedulesRepository;
        mCurrentMonth.setValue(String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1));
    }

    public void start() {
        loadHolidaySchedules();
        reloadSchedules(true);
    }

    private void loadHolidaySchedules() {
        //TODO firestoreでクラッシュする場合はコメントアウトする
        mSchedulesRepository.getHoliday(this);
    }

    public void loadSchedules(Map<String, List<Schedule>> result) {
        Schedules.setValue(result);
    }

    public void setCurrentMonth(Integer month) {
        mCurrentMonth.setValue(String.valueOf(month) + "月");
    }

    public void setHolidaySchedules(Map<String, List<Schedule>> holidaySchedulesMap) {
        mHolidaySchedules.setValue(holidaySchedulesMap);
    }

    public void reloadSchedules(boolean forceUpdate) {
        if (forceUpdate) {
            mSchedulesRepository.cacheClear();
        }
        mSchedulesRepository.getSchedulesMap(this);
    }

    @Override
    public void onScheduleMapLoaded(Map<String, List<Schedule>> scheduleStringMap) {
        loadSchedules(scheduleStringMap);
    }

    public LiveData<Map<String, List<Schedule>>> getSchedules() {
        return Schedules;
    }

    public LiveData<Map<String, List<Schedule>>> getHolidaySchedules() {
        return mHolidaySchedules;
    }

    public LiveData<String> getCurrentMonth() {
        return mCurrentMonth;
    }

    @Override
    public void onScheduleLoaded(List<Schedule> schedules) {
        setHolidaySchedules(PigLeadUtils.getScheduleMapBySchedules(schedules));
    }

    @Override
    public void onDataNotAvailable() {

    }
}
