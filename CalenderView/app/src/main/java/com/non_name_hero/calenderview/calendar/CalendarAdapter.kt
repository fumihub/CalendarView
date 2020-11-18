package com.non_name_hero.calenderview.calendar

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.data.CalendarData
import com.non_name_hero.calenderview.databinding.CalendarCellBinding
import com.non_name_hero.calenderview.databinding.ScheduleListBinding
import com.non_name_hero.calenderview.utils.DateManager
import com.non_name_hero.calenderview.utils.PigLeadUtils
import java.util.*

class CalendarAdapter(private val context: Context, month: Int) : BaseAdapter() {
    /**
     * @return dateArray
     */
    val dateArray: List<Date>
    private val mDateManager: DateManager
    private val mLayoutInflater: LayoutInflater
    private var mCalendarMap: Map<String, List<CalendarData>>
    private var mHolidayMap: Map<String, List<CalendarData>>

    /**
     * カレンダー表示する際に使用する日数
     *
     * @return dateManagerから取得した日数が返却される
     */
    override fun getCount(): Int {
        return dateArray.size
    }

    /**
     * Adapterクラスのメソッド
     *
     * @param position    {int}
     * @param convertView {View}
     * @param parent
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val calendarCellBinding: CalendarCellBinding
        val cellDate = dateArray[position]
        calendarCellBinding = if (convertView == null) {
            val inflater = LayoutInflater.from(parent.context)
            DataBindingUtil.inflate(inflater, R.layout.calendar_cell, parent, false)
        } else {
            DataBindingUtil.getBinding(convertView) ?: throw IllegalStateException()
        }

        //セルのサイズを指定
        val dp = context.resources.displayMetrics.density
        val params = AbsListView.LayoutParams(parent.width / 7 - dp.toInt(), (parent.height - dp.toInt() * mDateManager.weeks) / mDateManager.weeks)
        calendarCellBinding.calendarCell.layoutParams = params

        //日付のみ表示させる
        calendarCellBinding.date = PigLeadUtils.formatD.format(cellDate)

        //当月以外のセルをグレーアウト
        if (mDateManager.isCurrentMonth(cellDate)) {
            //当日の背景を黄色に
            if (mDateManager.currentDate == cellDate) {
                calendarCellBinding.calendarCell.setBackgroundColor(ContextCompat.getColor(context, R.color.currentDayColor))
            } else {
                calendarCellBinding.calendarCell.setBackgroundColor(Color.WHITE)
            }
        } else {
            calendarCellBinding.calendarCell.setBackgroundColor(ContextCompat.getColor(context, R.color.notCurrentMonth))
        }

        //祝日、日曜日を赤、土曜日を青に
        setCalendarCellBase(
                calendarCellBinding,
                PigLeadUtils.formatYYYYMMDD.format(cellDate),
                mDateManager.getDayOfWeek(cellDate)
        )

        //スケジュールをセルに追記
        val scheduleList = mCalendarMap.get(PigLeadUtils.formatYYYYMMDD.format(cellDate))
                ?: emptyList()
        //スケジュールを追加
        for (i in 0..3) {
            if (mCalendarMap.containsKey(PigLeadUtils.formatYYYYMMDD.format(cellDate)) && i < scheduleList.size) {
                setScheduleText(scheduleList[i], calendarCellBinding.scheduleList)
            } else {
                break
            }
        }


        //ビューを即時更新
        calendarCellBinding.executePendingBindings()
        return calendarCellBinding.root
    }

    /**
     * カレンダーセルベースを作成
     */
    private fun setCalendarCellBase(view: CalendarCellBinding?, date: String, dayOfWeek: Int) {
        view!!.scheduleList.removeAllViews()
        var schedules: List<CalendarData>
        var isHoliday = false
        if (mHolidayMap.containsKey(date)) {
            schedules = mHolidayMap.get(date) ?: emptyList()
            for (schedule in schedules) {
                //祝日の場合
                setScheduleText(schedule, view.scheduleList)
            }
            isHoliday = true
        }

        //日曜日の場合
        if (dayOfWeek == 1 || true == isHoliday) {
            view.dateText.setTextColor(Color.RED)
        } else if (dayOfWeek == 7) {
            view.dateText.setTextColor(Color.BLUE)
        } else {
            view.dateText.setTextColor(Color.BLACK)
        }
    }

    /**
     * スケジュールをセットする
     *
     * @param schedule
     * @param root
     * @return textView
     */
    private fun setScheduleText(schedule: CalendarData, root: ViewGroup) {
        val binding = ScheduleListBinding.inflate(mLayoutInflater, root, true)
        binding.schedule = schedule
        binding.executePendingBindings()
        //Drawableで背景を指定
        val drawable = GradientDrawable()
        drawable.cornerRadius = 10f
        if (schedule.isHoliday) {
            drawable.setColor(ContextCompat.getColor(context, R.color.holidayColor))
        } else {
            drawable.setColor(schedule.groupBackgroundColor)
        }
        binding.root.background = drawable
    }

    /*拡張機能*/
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    fun replaceData(schedules: Map<String, List<CalendarData>>) {
        setCalendarMap(schedules)
    }

    fun replaceHoliday(holidayMap: Map<String, List<CalendarData>>) {
        setHolidayMap(holidayMap)
    }

    private fun setHolidayMap(holidayMap: Map<String, List<CalendarData>>) {
        mHolidayMap = holidayMap
        notifyDataSetChanged()
    }

    private fun setCalendarMap(calendarMap: Map<String, List<CalendarData>>) {
        mCalendarMap = calendarMap
        notifyDataSetChanged()
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mDateManager = DateManager()
        mDateManager.jumpMonth(month)
        dateArray = mDateManager.days
        mCalendarMap = HashMap()
        mHolidayMap = HashMap()
    }
}