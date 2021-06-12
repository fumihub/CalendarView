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

class CalendarViewModel(private val schedulesRepository: ScheduleRepository) : ViewModel(), GetScheduleCallback, LoadCalendarDataCallback, LoadHolidayCalendarDataCallback {
    // for calendar
    val schedules = MutableLiveData<Map<String, List<Schedule>>>()

    private val _holidayCalendarDataMap = MutableLiveData<Map<String, List<CalendarData>>>()
    val holidaySchedulesMap: LiveData<Map<String, List<CalendarData>>>
        get() = _holidayCalendarDataMap

    private val _calendarDataMap = MutableLiveData<Map<String, List<CalendarData>>>()
    val calendarDataMap: LiveData<Map<String, List<CalendarData>>>
        get() = _calendarDataMap

    private val _currentMonth = MutableLiveData<Int>()
    val currentMonth: LiveData<Int>
        get() = _currentMonth

    private val _currentYearMonth = MutableLiveData<String>()
    val currentYearMonth: LiveData<String>
        get() = _currentYearMonth

    // for scheduleList
    private val selectedDate = MutableLiveData<Date>()

    val _scheduleListData = MutableLiveData<List<CalendarData>>().apply { value = mutableListOf<CalendarData>() }
    val scheduleListData: LiveData<List<CalendarData>>
        get() = _scheduleListData
    //set(value){ _scheduleListData.value = value.value}

    /**
     * 現在のカレンダーモード
     */
    private val _currentMode = MutableLiveData<Boolean>().apply { this.value = true }
    val currentMode: LiveData<Boolean>
        get() = _currentMode

    fun start() {
        loadHolidaySchedules()
        reloadCalendarData(true)
    }

    fun reloadCalendarData(forceUpdate: Boolean) {
        if (forceUpdate) {
            schedulesRepository.calendarCacheClear()
        }
        schedulesRepository.getCalendarDataList(this)
    }

    private fun loadHolidaySchedules() {
        //TODO firestoreでクラッシュする場合はコメントアウトする
        schedulesRepository.getHoliday(this)
    }

    // setters
    fun setCalendarDataMap(calendarDataMap: Map<String, MutableList<CalendarData>>) {
        this._calendarDataMap.value = calendarDataMap
    }

    fun setCurrentMonth(month: Int) {
        this._currentMonth.value = month
    }

    fun setHolidayCalendarDataMap(holidaySchedulesMap: Map<String, MutableList<CalendarData>>) {
        this._holidayCalendarDataMap.value = holidaySchedulesMap
    }

    fun setCurrentMode(mode: Boolean) {
        this._currentMode.value = mode
    }

    fun setCurrentYearMonth(offsetMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, offsetMonth)
        val month = calendar[Calendar.MONTH] + 1
        _currentYearMonth.value = createYearMonthWording(calendar[Calendar.YEAR], month)
        _currentMonth.value = month
    }

    private fun createYearMonthWording(year: Int, month: Int): String {
        return "$year / $month"
    }

    //callback
    override fun onScheduleLoaded(schedules: List<Schedule>) {}
    override fun onCalendarDataLoaded(calendarDataList: List<CalendarData>) {
        setCalendarDataMap(PigLeadUtils.getCalendarDataMapByCalendarDataList(calendarDataList))
        selectedDate.value?.let { setScheduleItem(it) }
    }

    override fun onHolidayCalendarDataLoaded(calendarDataList: List<CalendarData>) {
        setHolidayCalendarDataMap(PigLeadUtils.getCalendarDataMapByCalendarDataList(calendarDataList))
    }

    override fun onDataNotAvailable() {}

    // etc
    fun setScheduleItem(date: Date) {
        if (this.calendarDataMap != null) {
            val dateKey = PigLeadUtils.formatYYYYMMDD.format(date)
            if (this.calendarDataMap.value?.containsValue(dateKey) ?: false) {
                //tureならvalueがnullではない
                _scheduleListData.value = this.calendarDataMap.value?.get(dateKey)
            }

            selectedDate.value = date
        }
    }

    fun saveScheduleItem() {}
    fun removeSchedule(scheduleId: Long) {
        schedulesRepository.removeScheduleByScheduleId(scheduleId)
        reloadCalendarData(true)
    }

    init {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        _currentMonth.value = calendar[Calendar.MONTH] + 1
        selectedDate.value = Date()
        _currentYearMonth.value = createYearMonthWording(calendar[Calendar.YEAR], calendar[Calendar.MONTH] + 1)
    }
}