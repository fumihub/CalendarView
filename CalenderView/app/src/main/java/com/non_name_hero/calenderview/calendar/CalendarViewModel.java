package com.non_name_hero.calenderview.calendar;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.non_name_hero.calenderview.data.Schedule;
import com.non_name_hero.calenderview.data.source.ScheduleDataSource;
import com.non_name_hero.calenderview.data.source.ScheduleRepository;

import java.util.List;
import java.util.Map;

public class CalendarViewModel extends ViewModel  implements ScheduleDataSource.GetScheduleMapCallback {
    private MutableLiveData<Map<String,String>> Schedules;
    private MutableLiveData<List<Schedule>> mSchedulesLiveData;
    private ScheduleRepository mSchedulesRepository;

    public CalendarViewModel(ScheduleRepository SchedulesRepository){
        this.mSchedulesRepository = SchedulesRepository;
    }

    public void start(){
        mSchedulesRepository.getSchedulesMap(this);
    }

    public MutableLiveData<Map<String,String>> getSchedules(){
        if (Schedules == null){
            Schedules = new MutableLiveData<Map<String, String>>();
        }
        return Schedules;
    }

    public void loadSchedules(Map<String,String> result){
        Schedules.setValue(result);
    }

    @Override
    public void onScheduleMapLoaded(Map<String, String> scheduleStringMap) {
        Schedules.setValue(scheduleStringMap);
    }

    public void setSchedules(List<Schedule> schedules) {
        this.mSchedulesLiveData.setValue(schedules);
    }
}
