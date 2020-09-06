package com.non_name_hero.calenderview.calendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.non_name_hero.calenderview.R;
import com.non_name_hero.calenderview.data.Schedule;
import com.non_name_hero.calenderview.databinding.CalendarCellBinding;
import com.non_name_hero.calenderview.databinding.ScheduleListBinding;
import com.non_name_hero.calenderview.utils.DateManager;
import com.non_name_hero.calenderview.utils.PigLeadUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarAdapter extends BaseAdapter {
    private List<Date> dateArray;
    private Context mContext;
    private DateManager mDateManager;
    private LayoutInflater mLayoutInflater;
    private Map<String, List<Schedule>> mCalendarMap;
    private Map<String, List<Schedule>> mHolidayMap;

    public CalendarAdapter(Context context, int month) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDateManager = new DateManager();
        mDateManager.jumpMonth(month);
        dateArray = mDateManager.getDays();
        mCalendarMap = new HashMap<>();
        mHolidayMap = new HashMap<>();
    }

    /**
     * @return dateArray
     */
    public List<Date> getDateArray() {
        return dateArray;
    }

    /**
     * カレンダー表示する際に使用する日数
     *
     * @return dateManagerから取得した日数が返却される
     */
    @Override
    public int getCount() {
        return dateArray.size();
    }

    /**
     * Adapterクラスのメソッド
     *
     * @param position    {int}
     * @param convertView {View}
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CalendarCellBinding calendarCellBinding;
        Date cellDate = dateArray.get(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            calendarCellBinding = DataBindingUtil.inflate(inflater, R.layout.calendar_cell, parent, false);
        } else {
            calendarCellBinding = DataBindingUtil.getBinding(convertView);
        }

        //セルのサイズを指定
        float dp = mContext.getResources().getDisplayMetrics().density;
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(parent.getWidth() / 7 - (int) dp, (parent.getHeight() - (int) dp * mDateManager.getWeeks()) / mDateManager.getWeeks());
        calendarCellBinding.calendarCell.setLayoutParams(params);

        //日付のみ表示させる
        calendarCellBinding.setDate(PigLeadUtils.formatD.format(cellDate));

        //当月以外のセルをグレーアウト
        if (mDateManager.isCurrentMonth(cellDate)) {
            //当日の背景を黄色に
            if (mDateManager.getCurrentDate().equals(cellDate)) {
                calendarCellBinding.calendarCell.setBackgroundColor(Color.YELLOW);
            } else {
                calendarCellBinding.calendarCell.setBackgroundColor(Color.WHITE);
            }
        } else {
            calendarCellBinding.calendarCell.setBackgroundColor(Color.LTGRAY);
        }

        //祝日、日曜日を赤、土曜日を青に
        setCalendarCellBase(
                calendarCellBinding,
                PigLeadUtils.formatYYYYMMDD.format(cellDate),
                mDateManager.getDayOfWeek(cellDate)
        );

        //スケジュールをセルに追記
        if (mCalendarMap != null) {
            List<Schedule> scheduleList = mCalendarMap.get(PigLeadUtils.formatYYYYMMDD.format(cellDate));
            //スケジュールを追加
            for (int i = 0; i < 4; i++) {
                if (mCalendarMap.containsKey(PigLeadUtils.formatYYYYMMDD.format(cellDate)) && i < scheduleList.size()) {
                    setScheduleText(scheduleList.get(i), calendarCellBinding.scheduleList);
                } else {
                    break;
                }
            }
        }

        //ビューを即時更新
        calendarCellBinding.executePendingBindings();
        return calendarCellBinding.getRoot();
    }

    /**
     * カレンダーセルベースを作成
     */
    private void setCalendarCellBase(CalendarCellBinding view, String date, int dayOfWeek) {
        view.scheduleList.removeAllViews();
        List<Schedule> schedules = new ArrayList<>();
        Boolean isHoliday = Boolean.FALSE;
        if (mHolidayMap != null && mHolidayMap.containsKey(date)) {
            schedules = mHolidayMap.get(date);
            for (Schedule schedule : schedules) {
                //祝日の場合
                setScheduleText(schedule, view.scheduleList);
            }
            isHoliday = Boolean.TRUE;
        }

        //日曜日の場合
        if (dayOfWeek == 1 || Boolean.TRUE == isHoliday) {
            view.dateText.setTextColor(Color.RED);
        }
        //土曜日の場合
        else if (dayOfWeek == 7) {
            view.dateText.setTextColor(Color.BLUE);
        }
        //平日の場合
        else {
            view.dateText.setTextColor(Color.BLACK);
        }

    }

    /**
     * スケジュールをセットする
     *
     * @param schedule
     * @param root
     * @return textView
     */
    private void setScheduleText(Schedule schedule, ViewGroup root) {
        ScheduleListBinding binding = ScheduleListBinding.inflate(mLayoutInflater, root, true);
        binding.setSchedule(schedule);
        binding.executePendingBindings();
        //Drawableで背景を指定
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(10);
        if (schedule.getIsHoliday()) {
            drawable.setColor(mContext.getResources().getColor(R.color.holidayColor));
        } else {
            //TODO スケジュールの色を取得
            drawable.setColor(Color.parseColor("#600000FF"));
        }
        binding.getRoot().setBackground(drawable);

    }

    //holidayTextのリフレッシュ
    private void refreshHoliday(CalendarCellBinding binding) {
        binding.dateText.setVisibility(View.GONE);
    }

    /*拡張機能*/
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    //表示月を取得
    public String getTitle() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM", Locale.US);
        return format.format(mDateManager.getCalendar().getTime());
    }

    public void replaceData(Map<String, List<Schedule>> schedules) {
        setCalendarMap(schedules);
    }

    public void replaceHoliday(Map<String, List<Schedule>> holidayMap) {
        setHolidayMap(holidayMap);
    }

    private void setHolidayMap(Map<String, List<Schedule>> holidayMap) {
        this.mHolidayMap = holidayMap;
        notifyDataSetChanged();
    }

    private void setCalendarMap(Map<String, List<Schedule>> calendarMap) {
        this.mCalendarMap = calendarMap;
        notifyDataSetChanged();
    }
}

