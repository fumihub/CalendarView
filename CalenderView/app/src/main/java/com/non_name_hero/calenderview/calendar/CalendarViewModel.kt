package com.non_name_hero.calenderview.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.non_name_hero.calenderview.data.BalanceCategoryData
import com.non_name_hero.calenderview.data.BalanceData
import com.non_name_hero.calenderview.data.CalendarData
import com.non_name_hero.calenderview.data.Schedule
import com.non_name_hero.calenderview.data.source.ScheduleDataSource.*
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.utils.BalanceType
import com.non_name_hero.calenderview.utils.PigLeadUtils
import java.util.*

class CalendarViewModel(private val schedulesRepository: ScheduleRepository) : ViewModel(), GetScheduleCallback, LoadCalendarDataCallback, LoadHolidayCalendarDataCallback, GetBalanceDataCallback {
    // for calendar
    val schedules = MutableLiveData<Map<String, List<Schedule>>>()

    private val _holidayCalendarDataMap = MutableLiveData<Map<String, List<CalendarData>>>()
    val holidaySchedulesMap: LiveData<Map<String, List<CalendarData>>>
        get() = _holidayCalendarDataMap

    private val _calendarDataMap = MutableLiveData<Map<String, List<CalendarData>>>()
    val calendarDataMap: LiveData<Map<String, List<CalendarData>>>
        get() = _calendarDataMap

    private val _currentDate = MutableLiveData<Date>()
    val currentDate: LiveData<Date>
        get() = _currentDate

    private val _currentMonth = MutableLiveData<Int>()
    val currentMonth: LiveData<Int>
        get() = _currentMonth

    private val _currentYearMonth = MutableLiveData<String>()
    val currentYearMonth: LiveData<String>
        get() = _currentYearMonth

    // for scheduleList
    private val _selectedDate = MutableLiveData<Date>()
    val selectedDate: LiveData<Date>
        get() = _selectedDate


    private val _scheduleListData = MutableLiveData<List<CalendarData>>().apply {
        value = mutableListOf<CalendarData>()
    }
    val scheduleListData: LiveData<List<CalendarData>>
        get() = _scheduleListData
    //set(value){ _scheduleListData.value = value.value}

    // for Calendar Balance mode
    private var _balanceCategoryListData = MutableLiveData<List<BalanceCategoryData>>().apply {
        value = mutableListOf<BalanceCategoryData>()
    }
    val balanceCategoryListData: LiveData<List<BalanceCategoryData>>
        get() = _balanceCategoryListData

    private var _balanceListData: LiveData<List<BalanceData>> = MutableLiveData<List<BalanceData>>().apply {
        value = mutableListOf<BalanceData>()
    }
    val balanceListData: LiveData<List<BalanceData>>
        get() = _balanceListData

    private val _balanceDataMap = MutableLiveData<Map<String, List<BalanceData>>>(mapOf())
    val balanceDataMap: LiveData<Map<String, List<BalanceData>>>
        get() = _balanceDataMap

    // Livedataが変更された場合に実行される処理を定義
    private val balanceDataListLiveDataObserver =
            Observer<List<BalanceData>> { balanceDataList ->
                _balanceDataMap.value = PigLeadUtils.getBalanceCalendarDataMapByBalanceDataList(balanceDataList)
            }

    /**
     * 月ごとの収支サマリーデータ
     * Map: 年月: 集計された収支サマリーデータ
     */
    private val _balanceSummaryMap = MutableLiveData<Map<String, List<BalanceData>>>()
    val balanceSummaryMap: LiveData<Map<String, List<BalanceData>>>
        get() = _balanceSummaryMap

    /**
     * 現在のカレンダーモード
     * value = false の時にカレンダーモード
     *         true の時に家計簿モード
     */
    private val _currentMode = MutableLiveData<Boolean>().apply { this.value = false }
    val currentMode: LiveData<Boolean>
        get() = _currentMode

    fun start() {
        loadHolidaySchedules()
        reloadCalendarData(true)
        reloadBalanceData()
        setBalanceItem(Date())
        reloadBalanceSummary(Date())
    }

    /**
     * 家計簿データの取得処理
     */
    fun reloadBalanceSummary(yearMonth:Date? = null) {
        schedulesRepository.getBalanceSummary(yearMonth= yearMonth, object : GetBalanceSummaryCallback {
            override fun onBalanceDataLoaded(balanceData: List<BalanceData>) {
                // サマリーMapの生成
                val map = mutableMapOf<String, List<BalanceData>>()
                balanceData.forEach {
                    if (map.containsKey(it.timestamp)) {
                        map[it.timestamp] = map[it.timestamp]!! + listOf(it)
                    } else {
                        map[it.timestamp] = listOf(it)
                    }
                }
                _balanceSummaryMap.value = map
            }

            override fun onDataNotAvailable() {
                Log.w("warn", "fail get balance summary or Empty Data")
            }
        })

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
        _currentDate.value = calendar.time
    }

    fun getPageYearMonth(offsetMonth: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, offsetMonth)
        return calendar.time
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

    /**
     * 家計簿データの取得
     */
    fun reloadBalanceData(startMonth: Date? = null, forceUpdate: Boolean = false) {
        if (forceUpdate) {
            // TODO LiveDataでは不要？
            schedulesRepository.balanceDataCacheClear()
        }
        //TODO: 月指定で取得処理をする
//        val calendar = Calendar.getInstance()
//        calendar.time = startMonth
//        calendar.set(Calendar.DAY_OF_MONTH, 0)
//        val startTargetMonth = calendar.time
//        calendar.add(Calendar.MONTH, 1)
//        val endTargetMonth = calendar.time
        schedulesRepository.getBalanceData(startMonth = null, endMonth = null, this)
    }

    /**
     * roomから家計簿データを取得時のcallback
     */
    override fun onBalanceDataLoaded(balanceData: List<BalanceData>) {
        Log.d("balanceData", balanceData.toString())
        _balanceDataMap.value = PigLeadUtils.getBalanceCalendarDataMapByBalanceDataList(balanceData)
        // Livedataの監視
//        balanceListData.observeForever(balanceDataListLiveDataObserver)
    }

    // etc
    fun setScheduleItem(date: Date) {
        val dateKey = PigLeadUtils.formatYYYYMMDD.format(date)
        if (this.calendarDataMap.value?.containsKey(dateKey) == true) {
            //tureならvalueがnullではない
            _scheduleListData.value = this.calendarDataMap.value?.get(dateKey)
        } else {
            _scheduleListData.value = mutableListOf<CalendarData>()
        }

        _selectedDate.value = date
    }

    fun setBalanceItem(date: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val start = calendar.time
        calendar.add(Calendar.MONTH, 1)
        val end = calendar.time
        schedulesRepository.balanceDataCacheClear()
        schedulesRepository.getBalanceCategoryData(startMonth = start, endMonth = end, object : GetBalanceCategoryDataCallback {
            override fun onBalanceCategoryDataLoaded(balanceCategoryData: List<BalanceCategoryData>) {
                _balanceCategoryListData.value = balanceCategoryData
            }

            override fun onDataNotAvailable() {
                Log.e("balanceCategoryData", "balanceCategoryData not available")
            }
        })
        // 月間サマリー取得
        _selectedDate.value = date
    }

    fun removeSchedule(scheduleId: Long) {
        schedulesRepository.removeScheduleByScheduleId(scheduleId)
        reloadCalendarData(true)
    }

//    fun removeBalance(balanceId: Long) {
//        schedulesRepository.removeBalanceByBalanceId(balanceId)
//        reloadBalanceData()
//    }

    init {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        _currentMonth.value = calendar[Calendar.MONTH] + 1
        _selectedDate.value = Date()
        _currentYearMonth.value = createYearMonthWording(calendar[Calendar.YEAR], calendar[Calendar.MONTH] + 1)
        _currentDate.value = Date()
    }

    override fun onCleared() {
//        balanceListData.removeObserver(balanceDataListLiveDataObserver)
        super.onCleared()
    }
}
