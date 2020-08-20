package com.non_name_hero.calenderview.data.source.local;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converter {
    @TypeConverter
    public static Date toDate(Long l) {return new Date(l); }

    @TypeConverter
    public static Long fromCalendar(Date date){
        return date == null ? null : date.getTime();
    }
}
