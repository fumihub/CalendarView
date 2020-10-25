package com.non_name_hero.calenderview.utils

import java.text.SimpleDateFormat
import java.util.*

//日付の操作を設定するクラス
class DateManager {
    private val mCalendar: Calendar
    private var startDate: Date? = null
    val currentDate: Date
    var holiday: Date? = null
    private var currentDayFlg = java.lang.Boolean.FALSE//日付を前月表示分巻き戻す

    //状態を復元
//今月の曜日から2引くことで前月の最後の曜日を取得(月曜を週始まりにしたことでずれが起こっているため、2をマイナス)
    //DAY_OF_WEEKには,0は存在しないため,0かマイナスの時は+7で正常な値に戻す。

    //当月の最初の曜日が月曜日以外の場合
//当月の日付を取得し、変更しない

    //当月のカレンダーに表示される前月分の日数を計算
    //日付に1日を設定
    //GridViewに表示するマスの合計を計算

    //DAY_OF_WEEK=SUNDAY:1,MONDAY:2,TUESDAY:3,WEDNESDAY:4,THURSDAY:5,FRIDAY:6,SATURDAY:7
//現在の状態を保持
    //当月の要素を取得
    val days: List<Date>
        get() {
            //現在の状態を保持
            startDate = mCalendar.time
            if (currentDayFlg === java.lang.Boolean.FALSE) {
                //当月の日付を取得し、変更しない
                currentDayFlg = java.lang.Boolean.TRUE
            }

            //当月のカレンダーに表示される前月分の日数を計算
            mCalendar[Calendar.DATE] = 1 //日付に1日を設定
            //GridViewに表示するマスの合計を計算
            val count = weeks * 7

            //DAY_OF_WEEK=SUNDAY:1,MONDAY:2,TUESDAY:3,WEDNESDAY:4,THURSDAY:5,FRIDAY:6,SATURDAY:7
            var dayOfWeek = mCalendar[Calendar.DAY_OF_WEEK] - 2 //今月の曜日から2引くことで前月の最後の曜日を取得(月曜を週始まりにしたことでずれが起こっているため、2をマイナス)
            //DAY_OF_WEEKには,0は存在しないため,0かマイナスの時は+7で正常な値に戻す。
            if (dayOfWeek <= 0) {
                dayOfWeek += 7
            }

            //当月の最初の曜日が月曜日以外の場合
            if (mCalendar[Calendar.DAY_OF_WEEK] != 2) {
                mCalendar.add(Calendar.DATE, -dayOfWeek) //日付を前月表示分巻き戻す
            }
            val days: MutableList<Date> = ArrayList()
            for (i in 0 until count) {
                days.add(mCalendar.time)
                mCalendar.add(Calendar.DATE, 1)
            }

            //状態を復元
            mCalendar.time = startDate
            return days
        }

    //当月かどうか確認
    fun isCurrentMonth(date: Date?): Boolean {
        val format = SimpleDateFormat("yyyy.MM", Locale.US)
        val currentMonth = format.format(mCalendar.time)
        return if (currentMonth == format.format(date)) {
            true
        } else {
            false
        }
    }//土日以外 /*mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH)*///日曜日の時 /*mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH)*//*mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH)*///12月 /*mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH)*///10月//8月//7月//5月//3月//1月//土曜日の時

    //MONTH = JANUARY(0), FEBRUARY(1), MARCH(2), APRIL(3), MAY(4), JUNE(5), JULY(6), AUGUST(7), SEPTEMBER(8), OCTOBER(9), NOVEMBER(10), DECEMBER(11)
//月の初めが月曜日の場合 /*mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH)*/ /*mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH)*///月の最終日が28日//2月//当月の要素を月初めの曜日を取得
    //日付に1日を設定
    //週数を取得(月始まりが、土日の場合6週表示(土曜日の場合31日の月のみ)、その他5週表示(2月は絶対5周)）
    //引数：dayOfWeek = 月初めの曜日
    val weeks: Int
        get() {
            //当月の要素を月初めの曜日を取得
            mCalendar[Calendar.DATE] = 1 //日付に1日を設定
            val dayOfWeek = mCalendar[Calendar.DAY_OF_WEEK]
            return if (mCalendar[Calendar.MONTH] == 1) { //2月
                if (mCalendar.getActualMaximum(Calendar.DATE) == 28) { //月の最終日が28日
                    if (dayOfWeek == 2) { //月の初めが月曜日の場合
                        return 4 /*mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH)*/
                    }
                }
                5 /*mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH)*/
            } else if (dayOfWeek == 7) { //土曜日の時
                //MONTH = JANUARY(0), FEBRUARY(1), MARCH(2), APRIL(3), MAY(4), JUNE(5), JULY(6), AUGUST(7), SEPTEMBER(8), OCTOBER(9), NOVEMBER(10), DECEMBER(11)
                if (mCalendar[Calendar.MONTH] == 0 //1月
                        || mCalendar[Calendar.MONTH] == 2 //3月
                        || mCalendar[Calendar.MONTH] == 4 //5月
                        || mCalendar[Calendar.MONTH] == 6 //7月
                        || mCalendar[Calendar.MONTH] == 7 //8月
                        || mCalendar[Calendar.MONTH] == 9 //10月
                        || mCalendar[Calendar.MONTH] == 11) { //12月
                    6 /*mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH)*/
                } else {
                    5 /*mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH)*/
                }
            } else if (dayOfWeek == 1) { //日曜日の時
                6 /*mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH)*/
            } else { //土日以外
                5 /*mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH)*/
            }
        }

    //曜日を取得
    fun getDayOfWeek(date: Date?): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.DAY_OF_WEEK]
    }

    fun getCalendar(): Calendar {
        return mCalendar
    }

    /* スライドで次月前月移動させたいためコメントアウト*/ //翌月へ
    fun nextMonth() {
        mCalendar.add(Calendar.MONTH, 1)
    }

    //前月へ
    fun prevMonth() {
        mCalendar.add(Calendar.MONTH, -1)
    }

    fun jumpMonth(jump: Int) {
        mCalendar.add(Calendar.MONTH, jump)
    }

    //コンストラクタ
    init {
        mCalendar = Calendar.getInstance()
        currentDate = mCalendar.time
    }
}