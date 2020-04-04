package com.non_name_hero.calenderview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//日付の操作を設定するクラス
public class DateManager {
    Calendar mCalendar;
    Date startDate;
    //コンストラクタ
    public DateManager(){
        mCalendar = Calendar.getInstance();
    }

    //当月の要素を取得
    public List<Date> getDays(){
        //現在の状態を保持
        startDate = mCalendar.getTime();
        //GridViewに表示するマスの合計を計算
        int count = getWeeks() * 7 ;

        //当月のカレンダーに表示される前月分の日数を計算
        mCalendar.set(Calendar.DATE, 1);//日付に1日を設定
        //DAY_OF_WEEK=SUNDAY:1,MONDAY:2,TUESDAY:3,WEDNESDAY:4,THURSDAY:5,FRIDAY:6,SATURDAY:7
        int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK) - 2;//今月の曜日から1引くことで前月の最後の曜日を取得
        mCalendar.add(Calendar.DATE, -dayOfWeek);//日付を前月表示分巻き戻す

        List<Date> days = new ArrayList<>();

        for (int i = 0; i < count; i ++){
            days.add(mCalendar.getTime());
            mCalendar.add(Calendar.DATE, 1);
        }

        //状態を復元
        mCalendar.setTime(startDate);

        return days;
    }

    //当月かどうか確認
    public boolean isCurrentMonth(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM", Locale.US);
        String currentMonth = format.format(mCalendar.getTime());
        if (currentMonth.equals(format.format(date))){
            return true;
        }else {
            return false;
        }
    }

    //週数を取得(月始まりが、金土日の場合6週表示(金曜日の場合31日の月のみ)、その他5周表示）
    public int getWeeks(){
        return 5/*mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH)*/;
    }

    //曜日を取得
    public int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /* スライドで次月前月移動させたいためコメントアウト*/
    //翌月へ
    public void nextMonth(){
        mCalendar.add(Calendar.MONTH, 1);
    }

    //前月へ
    public void prevMonth(){
        mCalendar.add(Calendar.MONTH, -1);
    }

}

