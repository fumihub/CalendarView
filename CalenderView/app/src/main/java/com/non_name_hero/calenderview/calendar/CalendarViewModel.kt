package com.non_name_hero.calenderview.calendar

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
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
        value = mutableListOf<CalendarData>() }
    val scheduleListData: LiveData<List<CalendarData>>
        get() = _scheduleListData
    //set(value){ _scheduleListData.value = value.value}

    // for Calendar Balance mode
    private var _balanceListData :LiveData<List<BalanceData>> = MutableLiveData<List<BalanceData>>().apply {
        value = mutableListOf<BalanceData>() }
    val balanceListData: LiveData<List<BalanceData>>
        get() = _balanceListData

    private val _balanceDataMap = MutableLiveData<Map<String, List<BalanceData>>>()
    val balanceDataMap: LiveData<Map<String, List<BalanceData>>>
        get() = _balanceDataMap

    // Livedataが変更された場合に実行される処理を定義
    private val balanceDataListLiveDataObserver =
        Observer<List<BalanceData>> { balanceDataList ->
            _balanceDataMap.value = PigLeadUtils.getBalanceCalendarDataMapByBalanceDataList(balanceDataList)
        }

    /**
     * 収入のサマリー
     */
    private val _balanceIncomeSummary = MutableLiveData<String>()
    val balanceIncomeSummary: LiveData<String>
        get() = _balanceIncomeSummary
    /**
     * 支出のサマリー
     */
    private val _balanceExpenseSummary = MutableLiveData<String>()
    val balanceExpenseSummary: LiveData<String>
        get() = _balanceExpenseSummary

    /**
     * 現在のカレンダーモード
     * value = true の時にカレンダーモード
     *         false の時に家計簿モード
     */
    private val _currentMode = MutableLiveData<Boolean>().apply { this.value = false }
    val currentMode: LiveData<Boolean>
        get() = _currentMode

    fun start() {
        loadHolidaySchedules()
        reloadCalendarData(true)
        reloadBalanceData()
        reloadBalanceSummary()
    }

    /**
     * 家計簿データの取得処理
     */
    private fun reloadBalanceSummary() {
        schedulesRepository.getBalanceSummary(object: GetBalanceSummaryCallback {
            override fun onBalanceDataLoaded(balanceData: List<BalanceData>) {
                balanceData.forEach { balanceData->
                    if(balanceData.balanceType === BalanceType.INCOME){
                        _balanceIncomeSummary.value = balanceData.priceText
                    }else if (balanceData.balanceType === BalanceType.EXPENSES){
                        _balanceExpenseSummary.value = balanceData.priceText
                    }
                }
            }
            override fun onDataNotAvailable() {
                Log.w("warn", "fail get balance summary or Empty Data" )
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
    private fun reloadBalanceData(startMonth: Date? = null, forceUpdate: Boolean = false){
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
        schedulesRepository.getBalanceData(startMonth = null,endMonth = null ,this)
    }

    /**
     * roomから家計簿データを取得時のcallback
     */
    override fun onBalanceDataLoaded(balanceLiveData: LiveData<List<BalanceData>>) {
        _balanceListData = balanceLiveData
        Log.d("balanceData", balanceLiveData.value.toString())
        // Livedataの監視
        balanceListData.observeForever(balanceDataListLiveDataObserver)
    }

    // etc
    fun setScheduleItem(date: Date) {
        val dateKey = PigLeadUtils.formatYYYYMMDD.format(date)
        if (this.calendarDataMap.value?.containsKey(dateKey) == true) {
            //tureならvalueがnullではない
            _scheduleListData.value = this.calendarDataMap.value?.get(dateKey)
        }else{
            _scheduleListData.value = mutableListOf<CalendarData>()
        }

        _selectedDate.value = date
    }

    fun removeSchedule(scheduleId: Long) {
        schedulesRepository.removeScheduleByScheduleId(scheduleId)
        reloadCalendarData(true)
    }

    init {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        _currentMonth.value = calendar[Calendar.MONTH] + 1
        _selectedDate.value = Date()
        _currentYearMonth.value = createYearMonthWording(calendar[Calendar.YEAR], calendar[Calendar.MONTH] + 1)
    }

    override fun onCleared() {
        balanceListData.removeObserver(balanceDataListLiveDataObserver)
        super.onCleared()
    }
}
