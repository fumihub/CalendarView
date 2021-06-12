package com.non_name_hero.calenderview.data.source.local

import androidx.room.TypeConverter
import java.util.*

object Converter {
    @JvmStatic
    @TypeConverter
    fun toDate(l: Long?): Date {
        return Date(l!!)
    }

    @JvmStatic
    @TypeConverter
    fun fromCalendar(date: Date?): Long? {
        return date?.time
    }
}