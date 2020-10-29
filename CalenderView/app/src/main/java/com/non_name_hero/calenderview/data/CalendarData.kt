package com.non_name_hero.calenderview.data

import androidx.room.Ignore
import java.util.*

class CalendarData {
    @JvmField
    var scheduleId: Long = 0
    @JvmField
    var scheduleTitle: String? = null
    @JvmField
    var scheduleStartAtDatetime: Date? = null
    @JvmField
    var scheduleEndAtDatetime: Date? = null
    @JvmField
    var groupId: Long = 0
    @JvmField
    var groupTextColor: String? = null
    @JvmField
    var groupColorNumber = 0
    @JvmField
    var groupBackgroundColor = 0

    @JvmField
    @Ignore
    var isHoliday = false
    val textColor: Boolean
        get() = groupTextColor == "黒";
}