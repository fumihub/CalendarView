package com.non_name_hero.calenderview.calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.non_name_hero.calenderview.R;
import com.non_name_hero.calenderview.utils.DateManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends BaseAdapter {
    private List<Date> dateArray;
    private Context mContext;
    private DateManager mDateManager;
    private LayoutInflater mLayoutInflater;

    //カスタムセルを拡張したらここでWigetを定義
    private static class ViewHolder {
        public TextView dateText;
        public TextView holidayText;
    }

    public CalendarAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDateManager = new DateManager();
        dateArray = mDateManager.getDays();
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
        ViewHolder holder;
        //初回時の処理 convertViewがnullの場合にはinflateしたViewを代入する。
        if (convertView == null) {
            //convertViewに
            convertView = mLayoutInflater.inflate(R.layout.calendar_cell, null);
            /*------カレンダーセルの作成-----*/
            holder = new ViewHolder();

            //日付
            holder.dateText = convertView.findViewById(R.id.dateText);
            //祝日名
            holder.holidayText = convertView.findViewById(R.id.holidayText);

            convertView.setTag(holder);
        } else {
            //convertViewがnullでなければconvertViewを再利用する。
            holder = (ViewHolder) convertView.getTag();
        }

        //セルのサイズを指定
        float dp = mContext.getResources().getDisplayMetrics().density;
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(parent.getWidth() / 7 - (int) dp, (parent.getHeight() - (int) dp * mDateManager.getWeeks()) / mDateManager.getWeeks());
        convertView.setLayoutParams(params);

        //日付のみ表示させる
        SimpleDateFormat dateFormat = new SimpleDateFormat("d", Locale.US);
        //holder.dateTextに日付をセットする。
        holder.dateText.setText(dateFormat.format(dateArray.get(position)));
        //mSelectedDate =

        //当月以外のセルをグレーアウト
        if (mDateManager.isCurrentMonth(dateArray.get(position))) {
            //当日の背景を黄色に
            if (mDateManager.getCurrentDate().equals(dateArray.get(position))) {
                convertView.setBackgroundColor(Color.YELLOW);
            } else {
                convertView.setBackgroundColor(Color.WHITE);
            }
        } else {
            convertView.setBackgroundColor(Color.LTGRAY);
        }

        //祝日、日曜日を赤、土曜日を青に
        judgeHoliday(holder, position, mDateManager.getDayOfWeek(dateArray.get(position)));


        return convertView;
    }

    //祝日を判定
    private void judgeHoliday(ViewHolder holder, int position, int day) {
        //祝日の場合
        if (mDateManager.getHoliday(dateArray.get(position)) != "") {
            holder.holidayText.setVisibility(View.VISIBLE);
            holder.holidayText.setTextColor(Color.WHITE);
            holder.holidayText.setText(mDateManager.getHoliday(dateArray.get(position)));
            holder.holidayText.setBackgroundColor(Color.RED);
            holder.dateText.setTextColor(Color.RED);
        } else {
            //日曜日の場合
            if (day == 1) {
                holder.dateText.setTextColor(Color.RED);
                refreshHoliday(holder);
            }
            //土曜日の場合
            else if (day == 7) {
                holder.dateText.setTextColor(Color.BLUE);
                refreshHoliday(holder);
            }
            //平日の場合
            else {
                holder.dateText.setTextColor(Color.BLACK);
                refreshHoliday(holder);
            }
        }
    }

    //holidayTextのリフレッシュ
    private void refreshHoliday(ViewHolder holder) {
        holder.holidayText.setVisibility(View.GONE);
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

    /**********/

    //表示月を取得
    public String getTitle() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM", Locale.US);
        return format.format(mDateManager.getCalendar().getTime());
    }


    /*次月前月はスライド式で表示したいため保留*/
    //翌月表示
    public void nextMonth() {
        mDateManager.nextMonth();
        dateArray = mDateManager.getDays();
        this.notifyDataSetChanged();
    }

    //前月表示
    public void prevMonth() {
        mDateManager.prevMonth();
        dateArray = mDateManager.getDays();
        this.notifyDataSetChanged();
    }

    public void setJumpMonth(int jump) {
        mDateManager.jumpMonth(jump);
        dateArray = mDateManager.getDays();
        this.notifyDataSetChanged();
    }
}
