package com.non_name_hero.calenderview.data;

import androidx.room.Ignore;

import java.util.Date;

public class CalendarData {
    public long scheduleId;
    public String scheduleTitle;
    public Date scheduleStartAtDatetime;
    public Date scheduleEndAtDatetime;
    public long groupId;
    public String groupTextColor;
    public int groupColorNumber;
    public int groupBackgroundColor;
    @Ignore
    public boolean isHoliday = false;
}
