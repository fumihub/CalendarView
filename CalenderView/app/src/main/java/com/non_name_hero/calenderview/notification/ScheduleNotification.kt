package com.non_name_hero.calenderview.notification;

import com.non_name_hero.calenderview.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Process
import android.util.Log
import androidx.core.app.NotificationCompat
import java.util.*
import com.non_name_hero.calenderview.calendar.MainActivity
import com.non_name_hero.calenderview.data.Schedule
import com.non_name_hero.calenderview.data.source.ScheduleDataSource
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.utils.Injection


class ScheduleNotification : BroadcastReceiver() {

    private lateinit var repository: ScheduleRepository     /**/

    private var listLen: Int = 0                            /*スケジュールの数*/

    private var scheduleMessage: String = ""                /*通知に表示させるメッセージ*/


    override fun onReceive(context: Context, intent: Intent) {

        repository = Injection.provideScheduleRepository(context)

        Log.d("AlarmBroadcastReceiver", "onReceive() pid=" + Process.myPid())
        val requestCode = intent.getIntExtra("RequestCode", 0)
        val scheduleIntent = Intent(context, MainActivity::class.java)
        val pendingScheduleIntent = PendingIntent.getActivity(context, requestCode, scheduleIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val channelId = "default"

        /*TODO　データベースから予定抽出して表示*/
        /*現在時間取得*/
        val calendar = Calendar.getInstance()
        /*日時をの次の日に設定*/
        calendar.add(Calendar.DATE, 1)
        /*「0時0分1秒」に設定*/
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 1)
        val targetStartDate: Date = calendar.time
        /*「23時59分59秒」に設定*/
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val targetEndDate: Date = calendar.time
        /*DB探索*/
        repository.pickUpSchedules(
                targetStartDate,
                targetEndDate,
                object : ScheduleDataSource.PickUpScheduleCallback {
                    override fun onScheduleLoaded(schedules: List<Schedule>) {
                        /*スケジュール数を取得*/
                        listLen = schedules.size
                        /*通知に表示させるメッセージを取得(複数ある場合は改行区切りで取得)*/
                        scheduleMessage = schedules.joinToString(separator = "\n")
                        /*TODO 時間を取得して時間も表示*/
                    }

                    override fun onDataNotAvailable() {}
                })

        val title = "明日の予定は" +listLen + "件です。"
        val scheduleNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        /*スケジュール確認NotificationChannel設定*/
        val scheduleChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        scheduleChannel.description = scheduleMessage
        scheduleChannel.enableVibration(true)
        scheduleChannel.canShowBadge()
        scheduleChannel.enableLights(true)
        scheduleChannel.lightColor = Color.BLUE
        /*the channel appears on the lockscreen*/
        scheduleChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        scheduleChannel.setSound(defaultSoundUri, null)
        scheduleChannel.setShowBadge(true)
        if (scheduleNotificationManager != null) {
            scheduleNotificationManager.createNotificationChannel(scheduleChannel)
            val notification = NotificationCompat.Builder(context, channelId)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.calendar_icon)
                    .setContentText(scheduleMessage)
                    .setAutoCancel(true)
                    .setContentIntent(pendingScheduleIntent)
                    .setWhen(System.currentTimeMillis())
                    .build()

            /*通知出力*/
            scheduleNotificationManager.notify(1, notification)

        }
    }
}