package com.non_name_hero.calenderview.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.non_name_hero.calenderview.data.CalendarData
import com.non_name_hero.calenderview.data.Schedule
import com.non_name_hero.calenderview.data.source.ScheduleDataSource.*
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.utils.PigLeadUtils
import java.util.*

class CalendarViewModel(private val mSchedulesRepository: ScheduleRepository?) : ViewModel(), GetScheduleCallback, LoadCalendarDataCallback, LoadHolidayCalendarDataCallback {
    // for calendar
    val schedules = MutableLiveData<Map<String, List<Schedule>>>()
    private val mHolidayCalendarDataMap = MutableLiveData<Map<String?, MutableList<CalendarData?>?>?>()
    private val mCalendarDataMap: MutableLiveData<Map<String?, MutableList<CalendarData?>?>?>? = MutableLiveData()
    private val mCurrentMonth = MutableLiveData<String>()
    private val currentMonth = MutableLiveData<Int?>()

    // for scheduleList
    private val selectedDate = MutableLiveData<Date?>()
    @kotlin.jvm.JvmField
    val scheduleListData = MutableLiveData<List<CalendarData?>?>()
    fun start() {
        loadHolidaySchedules()
        reloadCalendarData(true)
    }

    fun reloadCalendarData(forceUpdate: Boolean) {
        if (forceUpdate) {
            mSchedulesRepository!!.calendarCacheClear()
        }
        mSchedulesRepository!!.getCalendarDataList(this)
    }

    private fun loadHolidaySchedules() {
        //TODO firestoreでクラッシュする場合はコメントアウトする
        mSchedulesRepository!!.getHoliday(this)
    }

    // setters
    fun setCalendarDataMap(calendarDataMap: Map<String?, MutableList<CalendarData?>?>?) {
        mCalendarDataMap!!.value = calendarDataMap
    }

    fun setCurrentMonth(month: Int?) {
        currentMonth.value = month
    }

    fun setHolidayCalendarDataMap(holidaySchedulesMap: Map<String?, MutableList<CalendarData?>?>?) {
        mHolidayCalendarDataMap.value = holidaySchedulesMap
    }

    val calendarDataMap: LiveData<Map<String?, MutableList<CalendarData?>?>?>?
        get() = mCalendarDataMap
    val holidayCalendarDataMap: LiveData<Map<String?, MutableList<CalendarData?>?>?>
        get() = mHolidayCalendarDataMap

    fun getCurrentMonth(): LiveData<Int?> {
        return currentMonth
    }

    //callback
    override fun onScheduleLoaded(schedules: List<Schedule?>?) {}
    override fun onCalendarDataLoaded(calendarDataList: List<CalendarData?>?) {
        setCalendarDataMap(PigLeadUtils.getCalendarDataMapByCalendarDataList(calendarDataList))
        setScheduleItem(selectedDate.value)
    }

    override fun onHolidayCalendarDataLoaded(calendarDataList: List<CalendarData>) {
        setHolidayCalendarDataMap(PigLeadUtils.getCalendarDataMapByCalendarDataList(calendarDataList))
    }

    override fun onDataNotAvailable() {}

    // etc
    fun setScheduleItem(date: Date?) {
        if (mCalendarDataMap != null) {
            val dateKey = PigLeadUtils.formatYYYYMMDD.format(date)
            scheduleListData.value = mCalendarDataMap.value!![dateKey]
            selectedDate.value = date
        }
    }

    fun saveScheduleItem() {}
    fun removeSchedule(scheduleId: Long) {
        mSchedulesRepository!!.removeScheduleByScheduleId(scheduleId)
        reloadCalendarData(true)
    }

    init {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        currentMonth.value = calendar[Calendar.MONTH] + 1
        selectedDate.value = Date()
    }
}