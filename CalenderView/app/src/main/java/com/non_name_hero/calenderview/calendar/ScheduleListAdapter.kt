package com.non_name_hero.calenderview.calendar

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.calendar.ScheduleListAdapter.ItemViewHolder
import com.non_name_hero.calenderview.data.CalendarData
import com.non_name_hero.calenderview.databinding.ScheduleFragmentItemBinding
import java.util.*

class ScheduleListAdapter(private val context: Context, calendarViewModel: CalendarViewModel ) : RecyclerView.Adapter<ItemViewHolder>() {
    var calendarDataList: List<CalendarData>
    var currentMode: Boolean = false
    private val viewModel: CalendarViewModel

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //Bind処理
        val binding: ScheduleFragmentItemBinding = DataBindingUtil.bind(itemView) ?: throw IllegalStateException()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.schedule_fragment_item, parent, false)
        return ItemViewHolder(v)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        setDrawable(holder.binding, position)
        holder.binding.calendarData = calendarDataList[position]
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return calendarDataList.size
    }

    fun setCalendarDataForScheduleList(calendarData: List<CalendarData>) {
        calendarDataList = calendarData
        notifyDataSetChanged()
    }

    private fun setDrawable(binding: ScheduleFragmentItemBinding, position: Int) {
        val calendarData = calendarDataList[position]
        //Drawableで背景を指定
        val drawable = GradientDrawable()
        drawable.cornerRadius = 10f
        if (calendarData.isHoliday) {
            drawable.setColor(ContextCompat.getColor(context, R.color.holidayColor))
        } else {
            drawable.setColor(calendarData.groupBackgroundColor)
        }
        binding.root.background = drawable
    }

    fun removeScheduleItem(position: Int) {
        //TODO 削除処理
        val calendarData = calendarDataList[position]
        calendarDataList.toMutableList().removeAt(position)
        viewModel.removeSchedule(calendarData.scheduleId)
        notifyItemRangeRemoved(position, itemCount)
    }

    init {
        calendarDataList = ArrayList()
        viewModel = calendarViewModel
    }
}