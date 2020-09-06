package com.non_name_hero.calenderview.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//日付の操作を設定するクラス
public class DateManager {
    private Calendar mCalendar;
    private Date startDate;
    private Date currentDate;
    public Date holiday;
    private Boolean currentDayFlg = Boolean.FALSE;

    //コンストラクタ
    public DateManager(){
        mCalendar = Calendar.getInstance();
        currentDate = mCalendar.getTime();
    }

    //当月の要素を取得
    public List<Date> getDays(){
        //現在の状態を保持
        startDate = mCalendar.getTime();
        if (currentDayFlg == Boolean.FALSE) {
            //当月の日付を取得し、変更しない
            currentDayFlg = Boolean.TRUE;
        }

        //当月のカレンダーに表示される前月分の日数を計算
        mCalendar.set(Calendar.DATE, 1);//日付に1日を設定
        //GridViewに表示するマスの合計を計算
        int count = getWeeks() * 7 ;

        //DAY_OF_WEEK=SUNDAY:1,MONDAY:2,TUESDAY:3,WEDNESDAY:4,THURSDAY:5,FRIDAY:6,SATURDAY:7
        int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK) - 2;//今月の曜日から2引くことで前月の最後の曜日を取得(月曜を週始まりにしたことでずれが起こっているため、2をマイナス)
        //DAY_OF_WEEKには,0は存在しないため,0かマイナスの時は+7で正常な値に戻す。
        if (dayOfWeek <= 0) {
            dayOfWeek += 7;
        }

        //当月の最初の曜日が月曜日以外の場合
        if (mCalendar.get(Calendar.DAY_OF_WEEK) != 2) {
            mCalendar.add(Calendar.DATE, -dayOfWeek);//日付を前月表示分巻き戻す
        }

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

    //週数を取得(月始まりが、土日の場合6週表示(土曜日の場合31日の月のみ)、その他5週表示(2月は絶対5周)）
    //引数：dayOfWeek = 月初めの曜日
    public int getWeeks(){
        //当月の要素を月初めの曜日を取得
        mCalendar.set(Calendar.DATE, 1);//日付に1日を設定
        int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);

        if (mCalendar.get(Calendar.MONTH) == 1) {//2月
            if (mCalendar.getActualMaximum(Calendar.DATE) == 28) {//月の最終日が28日
                if (dayOfWeek == 2) {//月の初めが月曜日の場合
                    return 4/*mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH)*/;
                }
            }
            return 5/*mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH)*/;
        } else if (dayOfWeek == 7) {//土曜日の時
            //MONTH = JANUARY(0), FEBRUARY(1), MARCH(2), APRIL(3), MAY(4), JUNE(5), JULY(6), AUGUST(7), SEPTEMBER(8), OCTOBER(9), NOVEMBER(10), DECEMBER(11)
            if (mCalendar.get(Calendar.MONTH) == 0//1月
            || mCalendar.get(Calendar.MONTH) == 2//3月
            || mCalendar.get(Calendar.MONTH) == 4//5月
            || mCalendar.get(Calendar.MONTH) == 6//7月
            || mCalendar.get(Calendar.MONTH) == 7//8月
            || mCalendar.get(Calendar.MONTH) == 9//10月
            || mCalendar.get(Calendar.MONTH) == 11) {//12月
                return 6/*mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH)*/;
            }else{
                return 5/*mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH)*/;
            }
        }else if (dayOfWeek == 1) {//日曜日の時
            return 6/*mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH)*/;
        }else {//土日以外
            return 5/*mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH)*/;
        }
    }

    //曜日を取得
    public int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public Date getCurrentDate(){
        return currentDate;
    }
    public Calendar getCalendar(){
        return mCalendar;
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

    public void jumpMonth(int jump){mCalendar.add(Calendar.MONTH, jump);}
}

