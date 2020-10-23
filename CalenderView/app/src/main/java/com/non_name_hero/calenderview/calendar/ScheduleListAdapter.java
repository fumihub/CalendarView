package com.non_name_hero.calenderview.calendar;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.non_name_hero.calenderview.R;
import com.non_name_hero.calenderview.data.CalendarData;
import com.non_name_hero.calenderview.databinding.ScheduleFragmentItemBinding;

import java.util.ArrayList;
import java.util.List;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ItemViewHolder> {

    public List<CalendarData> calendarDataList;
    private Context context;
    private CalendarViewModel viewModel;

    public ScheduleListAdapter(Context appContext, CalendarViewModel calendarViewModel) {
        context = appContext;
        calendarDataList = new ArrayList<CalendarData>();
        viewModel = calendarViewModel;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private ScheduleFragmentItemBinding binding;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // Bind処理
            binding = DataBindingUtil.bind(itemView);
        }

        public ScheduleFragmentItemBinding getBinding() {
            return binding;
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_fragment_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        setDrawable(holder.getBinding(), position);
        holder.getBinding().setCalendarData(calendarDataList.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        int length = 0;
        if (calendarDataList != null) {
            length = calendarDataList.size();
        }
        return length;
    }

    public void setCalendarDataForScheduleList(List<CalendarData> calendarData) {
        calendarDataList = calendarData;
        notifyDataSetChanged();
    }

    private void setDrawable(ScheduleFragmentItemBinding binding, int position) {
        final CalendarData calendarData = calendarDataList.get(position);
        //Drawableで背景を指定
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(10);
        if (calendarData.isHoliday) {
            drawable.setColor(context.getResources().getColor(R.color.holidayColor));
        } else {
            drawable.setColor(calendarData.groupBackgroundColor);
        }
        binding.getRoot().setBackground(drawable);

    }

    public void removeScheduleItem(int position) {
        //TODO 削除処理
        CalendarData calendarData = calendarDataList.get(position);
        calendarDataList.remove(position);
        viewModel.removeSchedule(calendarData.scheduleId);
        notifyItemRangeRemoved(position, getItemCount());
    }
}
