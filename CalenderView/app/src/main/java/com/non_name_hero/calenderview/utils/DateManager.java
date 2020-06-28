package com.non_name_hero.calenderview.utils;

import android.content.ContentValues;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static androidx.constraintlayout.widget.Constraints.TAG;

//日付の操作を設定するクラス
public class DateManager {
    private Calendar mCalendar;
    private Date startDate;
    private Date currentDate;
    public Date holiday;
    private Boolean currentDayFlg = Boolean.FALSE;
    //リモートデータベース
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //データベースからの結果受け取り
    private Boolean result;

    //コンストラクタ
    public DateManager(){
        mCalendar = Calendar.getInstance();
    }

    /******************************************************/
    /* 機能    : 表示する前月、当月、後月の日にちを取得する */
    /*----------------------------------------------------*/
    /* 引数    : なし                                     */
    /*----------------------------------------------------*/
    /* 戻り値  : 表示する日にち                            */
    /******************************************************/
    public List<Date> getDays(){
        //現在の状態を保持
        startDate = mCalendar.getTime();
        if (currentDayFlg == Boolean.FALSE) {
            //当月の日付を取得し、変更しない
            currentDate = mCalendar.getTime();
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

    /************************************************************/
    /* 機能    : 当月か判定する                                  */
    /*----------------------------------------------------------*/
    /* 引数    : date 日にち                                    */
    /*----------------------------------------------------------*/
    /* 戻り値  : 判定結果                                       */
    /***********************************************************/
    public boolean isCurrentMonth(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM", Locale.US);
        String currentMonth = format.format(mCalendar.getTime());
        if (currentMonth.equals(format.format(date))){
            return true;
        }else {
            return false;
        }
    }

    /************************************************************************************************/
    /* 機能    : 週数を取得する                                                                      */
    /*          (月始まりが、土日の場合6週表示(土曜日の場合31日の月のみ)、その他5週表示(2月は絶対5周)) */
    /*----------------------------------------------------------------------------------------------*/
    /* 引数    : なし                                                                               */
    /*----------------------------------------------------------------------------------------------*/
    /* 戻り値  : 表示する週数                                                                        */
    /***********************************************************************************************/
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

    /******************************************************************************************/
    /* 機能    : 曜日を取得する                                                                */
    /*----------------------------------------------------------------------------------------*/
    /* 引数    : date 日にち                                                                  */
    /*----------------------------------------------------------------------------------------*/
    /* 戻り値  : 曜日(SUNDAY:1,MONDAY:2,TUESDAY:3,WEDNESDAY:4,THURSDAY:5,FRIDAY:6,SATURDAY:7) */
    /*****************************************************************************************/
    public int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /************************************************************/
    /* 機能    : 祝日を取得する                                  */
    /*----------------------------------------------------------*/
    /* 引数    : date 日にち                                    */
    /*----------------------------------------------------------*/
    /* 戻り値  : 祝日名(日本語)                                 */
    /***********************************************************/
    public String getHoliday (Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.US);
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy", Locale.US);
        int judgeSports = 0;

        //元日の場合
        if (judgeHoliday(formatYear.format(date),"NewYearDay",format.format(date))) {
            return "元日";
        }
        //成人の日の場合
        else if (judgeHoliday(formatYear.format(date),"ComingOfAgeDay",format.format(date))) {
            return "成人の日";
        }
        //建国記念の日の場合
        else if (judgeHoliday(formatYear.format(date),"NationalFoundationDay",format.format(date))) {
            return "建国記念の日";
        }
        //天皇誕生日の場合
        else if (judgeHoliday(formatYear.format(date),"BirthDayOfKing",format.format(date))) {
            return "天皇誕生日";
        }
        //春分の日の場合
        else if (judgeHoliday(formatYear.format(date),"DayOfSpring",format.format(date))) {
            return "春分の日";
        }
        //昭和の日の場合
        else if (judgeHoliday(formatYear.format(date),"DayOfShowa",format.format(date))) {
            return "昭和の日";
        }
        //憲法記念日の場合
        else if (judgeHoliday(formatYear.format(date),"ConstitutionMemorialDay",format.format(date))) {
            return "憲法記念日";
        }
        //みどりの日の場合
        else if (judgeHoliday(formatYear.format(date),"DayOfGreen",format.format(date))) {
            return "みどりの日";
        }
        //こどもの日の場合
        else if (judgeHoliday(formatYear.format(date),"DayOfChildren",format.format(date))) {
            return "こどもの日";
        }
        //海の日の場合
        else if (judgeHoliday(formatYear.format(date),"DayOfSea",format.format(date))) {
            return "海の日";
        }
        //スポーツの日の場合
        else if (judgeHoliday(formatYear.format(date),"DayOfSports",format.format(date))) {
            judgeSports = Integer.valueOf(formatYear.format(date));
            //2019年までの場合
            if (judgeSports <= 2019) {
                return "体育の日";
            }
            //2020年以降の場合
            else {
                return "スポーツの日";
            }
        }
        //山の日の場合
        else if (judgeHoliday(formatYear.format(date),"DayOfMountain",format.format(date))) {
            return "山の日";
        }
        //敬老の日の場合
        else if (judgeHoliday(formatYear.format(date),"RespectForTheAgedDay",format.format(date))) {
            return "敬老の日";
        }
        //秋分の日の場合
        else if (judgeHoliday(formatYear.format(date),"DayOfAutomn",format.format(date))) {
            return "秋分の日";
        }
        //文化の日の場合
        else if (judgeHoliday(formatYear.format(date),"CultualDay",format.format(date))) {
            return "文化の日";
        }
        //勤労感謝の日の場合
        else if (judgeHoliday(formatYear.format(date),"ThankYouForWorkingDay",format.format(date))) {
            return "勤労感謝の日";
        }
        //振替休日の場合
        else if ((judgeHoliday(formatYear.format(date),"SubstituteHoliday1",format.format(date)))
        || (judgeHoliday(formatYear.format(date),"SubstituteHoliday2",format.format(date)))
        || (judgeHoliday(formatYear.format(date),"SubstituteHoliday3",format.format(date)))
        || (judgeHoliday(formatYear.format(date),"SubstituteHoliday4",format.format(date)))
        || (judgeHoliday(formatYear.format(date),"SubstituteHoliday5",format.format(date)))) {
            return "振替休日";
        }
        //国民の休日の場合
        else if (((judgeHoliday(formatYear.format(date),"DayForJapanese1",format.format(date))))
        || ((judgeHoliday(formatYear.format(date),"DayForJapanese2",format.format(date))))) {
            return "国民の休日";
        }
        //新天皇即位の日の場合
        else if (judgeHoliday(formatYear.format(date),"BornNewKingDay",format.format(date))) {
            return "新天皇即位の日";
        }
        //即位礼正殿の儀の場合
        else if (judgeHoliday(formatYear.format(date),"CeremonyNewKingDay",format.format(date))) {
            return "即位礼正殿の儀";
        }
        //祝日じゃない時
        else {
            return "";
        }
    }

    /************************************************************/
    /* 機能    : 引数dateが祝日であるかを判断する                */
    /*----------------------------------------------------------*/
    /* 引数    : document データベースドキュメント名(年)         */
    /*         : holidayName フィールド名(祝日名(英語))         */
    /*         : date バリュー名(日にち)                        */
    /*----------------------------------------------------------*/
    /* 戻り値  : result 判定結果                                */
    /***********************************************************/
    private Boolean judgeHoliday(String document, final String holidayName, final String date){
        //resultの初期化
        result = false;

        //holidayデータベースを参照
        db.collection("holiday")
                .document(document)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            //documentにドキュメントデータがあればtrue
                            if (document.exists()) {
                                String value = document.getString(holidayName);//documentからholidayNameのバリューを取得
                                //valueとdateが一致したら=祝日の場合
                                if(value.equals(date)){
                                    result = true;
                                }
                                else {
                                    result = false;
                                }
                                //ログの出力：LogCatに表示される
                                Log.d(ContentValues.TAG, "DocumentSnapshot data: " + document.getData());
                            }
                            else {
                                Log.d(ContentValues.TAG, "No such document");
                            }
                        }
                        else {
                            Log.d(ContentValues.TAG, "get failed with ", task.getException());
                        }
                    }
                });

        return result;
    }

    public Date getCurrentDate(){
        return currentDate;
    }

    public Calendar getCalendar(){
        return mCalendar;
    }

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

