package com.non_name_hero.calenderview.inputForm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.notification.BalanceNotification
import com.non_name_hero.calenderview.notification.ScheduleNotification
import java.util.*

class NotificationSettingActivity  /*コンストラクタ*/
    : AppCompatActivity() {

    private lateinit var scheduleSpinner: Spinner              /*スケジュール通知設定スピナー*/
    private lateinit var balanceSpinner: Spinner               /*入出金通知設定スピナー*/
    private lateinit var cancelButton: Button               /*キャンセルボタン*/
    private lateinit var doneButton: Button                 /*保存ボタン*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*初期設定***************************************/
        setContentView(R.layout.notification_setting)
        val myToolbar = findViewById<View>(R.id.notificationSettingToolbar) as Toolbar
        setSupportActionBar(myToolbar)
        scheduleSpinner = findViewById(R.id.scheduleSpinner)
        balanceSpinner = findViewById(R.id.balanceSpinner)
        cancelButton = findViewById(R.id.cancelButton)
        doneButton = findViewById(R.id.doneButton)
        /************************************************/

        /*スピナー初期設定**********************************/
        val prefs = getSharedPreferences("input_data", MODE_PRIVATE)
        scheduleSpinner.setSelection(prefs.getInt("scheduleIdx", 0))
        balanceSpinner.setSelection(prefs.getInt("balanceIdx", 0))
        /************************************************/

        /*完了ボタンが押されたとき*******************/
        doneButton.setOnClickListener {
            /*保存処理を実行*/
            scheduleNotificationSetting()
            balanceNotificationSetting()
            finish()
        }
        /*********************************************/

        /*キャンセルボタンが押されたとき******************/
        cancelButton.setOnClickListener {
            /*カレンダー表示画面に遷移*/
            finish()
        }
        /*********************************************/

    }

    /*スケジュール通知設定関数****************************/
    private fun scheduleNotificationSetting() {

        /*スケジュール通知設定スピナー設定************************/
        val scheduleStr: String = scheduleSpinner.selectedItem.toString()
        val scheduleIdx: Int = scheduleSpinner.selectedItemPosition

        /*SharedPreferenceでscheduleIdxの値を変更*/
        val prefs = getSharedPreferences("input_data", MODE_PRIVATE)
        val editor = prefs.edit()
        /*SharedPreferenceにscheduleIdxの値を保存*/
        editor.putInt("scheduleIdx", scheduleIdx)
        /*非同期処理ならapply()、同期処理ならcommit()*/
        editor.apply()

        /*scheduleStrが「なし」の場合*/
        if (scheduleStr == "なし") {
            /*エラー出力*/
            outputToast("スケジュール通知の設定はされませんでした。")
        }
        /*scheduleStrが「なし」でない場合*/
        else {
            /*スケジュール指定時間通知システム***************************/
            val scheduleTime = intArrayOf(23, 22, 21, 20, 19, 18)
            val scheduleIntent: Intent = Intent(this, ScheduleNotification::class.java)

            /*Notificationを起動*/
            val scheduleSender = PendingIntent.getBroadcast(this, 0, scheduleIntent, 0)

            /*スピナーで入力された時間に設定*/
            val scheduleCalendar = Calendar.getInstance()
            scheduleCalendar.timeInMillis = System.currentTimeMillis()
            scheduleCalendar[Calendar.HOUR_OF_DAY] = scheduleTime[scheduleIdx - 1]/*Idx1が「なし」のため-1*/
            scheduleCalendar[Calendar.MINUTE] = 0
            scheduleCalendar[Calendar.SECOND] = 0
            scheduleCalendar[Calendar.MILLISECOND] = 0

            /*もし時間が過去の場合は1年先でセット*/
            if (scheduleCalendar.timeInMillis < System.currentTimeMillis()) {
                scheduleCalendar.add(Calendar.DAY_OF_YEAR, 1)
            }

            val scheduleAlarm = getSystemService(ALARM_SERVICE) as AlarmManager
            scheduleAlarm[AlarmManager.RTC_WAKEUP, scheduleCalendar.timeInMillis] = scheduleSender
            /*********************************************/

            /*確認ログ出力*/
            outputToast("スケジュール通知は「" + scheduleStr + "」に設定されました。")

        }
        /************************************************/
    }
    /*********************************************/

    /*入出金通知設定関数****************************/
    private fun balanceNotificationSetting() {

        /*入出金通知設定スピナー設定*************************/
        val balanceStr: String = balanceSpinner.selectedItem.toString()
        val balanceIdx: Int = balanceSpinner.selectedItemPosition

        /*SharedPreferenceでbalanceIdxの値を変更*/
        val prefs = getSharedPreferences("input_data", MODE_PRIVATE)
        val editor = prefs.edit()
        /*SharedPreferenceにbalanceIdxの値を保存*/
        editor.putInt("balanceIdx", balanceIdx)
        /*非同期処理ならapply()、同期処理ならcommit()*/
        editor.apply()

        /*balanceStrが「なし」の場合*/
        if (balanceStr == "なし") {
            /*エラー出力*/
            outputToast("入出金確認通知の設定はされませんでした。")
        }
        /*balanceStrが「なし」でない場合*/
        else {
            /*入出金指定時間通知システム***************************/
            val balanceTime = intArrayOf(23, 22, 21, 20, 19, 18)
            val balanceIntent: Intent = Intent(this, BalanceNotification::class.java)

            /*Notificationを起動*/
            val balanceSender = PendingIntent.getBroadcast(this, 0, balanceIntent, 0)

            /*スピナーで入力された時間に設定*/
            val balanceCalendar = Calendar.getInstance()
            balanceCalendar.timeInMillis = System.currentTimeMillis()
            balanceCalendar[Calendar.HOUR_OF_DAY] = balanceTime[balanceIdx - 1]/*Idx1が「なし」のため-1*/
            balanceCalendar[Calendar.MINUTE] = 0
            balanceCalendar[Calendar.SECOND] = 0
            balanceCalendar[Calendar.MILLISECOND] = 0

            /*もし時間が過去の場合は1年先でセット*/
            if (balanceCalendar.timeInMillis < System.currentTimeMillis()) {
                balanceCalendar.add(Calendar.DAY_OF_YEAR, 1)
            }

            val balanceAlarm = getSystemService(ALARM_SERVICE) as AlarmManager
            balanceAlarm[AlarmManager.RTC_WAKEUP, balanceCalendar.timeInMillis] = balanceSender
            /*********************************************/

            /*確認ログ出力*/
            outputToast("入出金確認通知は「" + balanceStr + "」に設定されました。")

        }
        /************************************************/
    }
    /************************************************/

    /*トースト出力関数************************************/
    private fun outputToast(str: String) {
        /*トースト表示*/
        val errorToast = Toast.makeText(
                applicationContext,
                str,
                Toast.LENGTH_SHORT
        )
        errorToast.show()
    }
    /************************************************/

}