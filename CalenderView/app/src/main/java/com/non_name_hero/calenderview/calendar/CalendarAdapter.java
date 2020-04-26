package com.non_name_hero.calenderview.calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.non_name_hero.calenderview.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends BaseAdapter {
    private List<Date> dateArray = new ArrayList();
    private Context mContext;
    private DateManager mDateManager;
    private LayoutInflater mLayoutInflater;
    private String mSelectedDate;

    //カスタムセルを拡張したらここでWigetを定義
    private static class ViewHolder {
        public TextView dateText;
    }

    public CalendarAdapter(Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDateManager = new DateManager();
        dateArray = mDateManager.getDays();
    }

    /**
     * カレンダー表示する際に使用する日数
     * @return dateManagerから取得した日数が返却される
     */
    @Override
    public int getCount() {
        return dateArray.size();
    }

    /**
     *Adapterクラスのメソッド
     * @param position {int}
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
            //holderインスタンスのdateTextにIDを格納
            holder.dateText = convertView.findViewById(R.id.dateText);
            /*------/カレンダーセルの作成-----*/
            convertView.setTag(holder);
        } else {
            //convertViewがnullでなければconvertViewを再利用する。
            holder = (ViewHolder)convertView.getTag();
        }

        //セルのサイズを指定
        float dp = mContext.getResources().getDisplayMetrics().density;
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(parent.getWidth()/7 - (int)dp, (parent.getHeight() - (int)dp * mDateManager.getWeeks() ) / mDateManager.getWeeks());
        convertView.setLayoutParams(params);

        //日付のみ表示させる
        SimpleDateFormat dateFormat = new SimpleDateFormat("d", Locale.US);
        //holder.dateTextに日付をセットする。
        holder.dateText.setText(dateFormat.format(dateArray.get(position)));
        //mSelectedDate =

        //当月以外のセルをグレーアウト
        if (mDateManager.isCurrentMonth(dateArray.get(position))){
            //当日の背景を黄色に
            if (mDateManager.currentDate.equals(dateArray.get(position))){
                convertView.setBackgroundColor(Color.YELLOW);
            }else{
                convertView.setBackgroundColor(Color.WHITE);
            }
        }else {
            convertView.setBackgroundColor(Color.LTGRAY);
        }

        //日曜日を赤、土曜日を青に
        int colorId;
        switch (mDateManager.getDayOfWeek(dateArray.get(position))){
            case 7:
                colorId = Color.BLUE;
                break;
            case 1:
                colorId = Color.RED;
                break;

            default:
                colorId = Color.BLACK;
                break;
        }
        holder.dateText.setTextColor(colorId);


        return convertView;
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

    public String getSelectedDate(int position){
        return mSelectedDate;
    }
/**********/

    //表示月を取得
    public String getTitle(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM", Locale.US);
        return format.format(mDateManager.mCalendar.getTime());
    }

    /*次月前月はスライド式で表示したいため保留*/
    //翌月表示
    public void nextMonth(){
        mDateManager.nextMonth();
        dateArray = mDateManager.getDays();
        this.notifyDataSetChanged();
    }

    //前月表示
    public void prevMonth(){
        mDateManager.prevMonth();
        dateArray = mDateManager.getDays();
        this.notifyDataSetChanged();
    }
}
