package com.non_name_hero.calenderview.data

import androidx.room.Ignore
import java.util.*

class CalendarData {

    var scheduleId: Long = 0

    var scheduleTitle: String? = null

    var scheduleStartAtDatetime: Date? = null

    var scheduleEndAtDatetime: Date? = null

    var groupId: Long = 0

    var groupTextColor: String? = null

    var groupColorNumber = 0

    var groupBackgroundColor = 0


    @Ignore
    var isHoliday = false


    val textColor: Boolean
        get() = this.groupTextColor == "黒";
}