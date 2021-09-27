package com.non_name_hero.calenderview.notification;

import android.annotation.SuppressLint
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
import android.text.Html.*
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.text.HtmlCompat
import com.google.firebase.FirebaseApp
import java.util.*
import com.non_name_hero.calenderview.calendar.MainActivity
import com.non_name_hero.calenderview.data.Schedule
import com.non_name_hero.calenderview.data.source.ScheduleDataSource
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.utils.Injection
import java.text.SimpleDateFormat


class ScheduleNotification : BroadcastReceiver() {

    private lateinit var repository: ScheduleRepository     /**/

    override fun onReceive(context: Context, intent: Intent) {

        /*FireBaseの初期化*/
        FirebaseApp.initializeApp(context)

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
                    @SuppressLint("WrongConstant")
                    override fun onScheduleLoaded(schedules: List<Schedule>) {
                        /*スケジュール数を取得*/
                        val html = "<html><b>明日の予定は " + schedules.size + " 件です。</b></html>"
                        val title = HtmlCompat.fromHtml(html, FROM_HTML_MODE_COMPACT)
                        /*通知に表示させるメッセージを取得(複数ある場合は改行区切り)
                        * 終日予定の場合：「終日　タイトル」
                        * 時間指定の場合：「startTime-endTime　タイトル」*/
                        val scheduleMessageList  = mutableListOf<String>()
                        /*時間指定用フォーマット*/
                        val df = SimpleDateFormat("HH:mm")
                        /*スケジュールの要素が0でなければ*/
                        if (schedules.isNotEmpty()) {
                            /*空白行追加*/
                            scheduleMessageList.add("")
                            /*スケジュールの要素を１つずつ取り出し文字列連結*/
                            schedules.forEach {
                                /*時間指定がある場合*/
                                if (it.timeSettingFlag) {
                                    scheduleMessageList.add(df.format(it.startAtDatetime) + "-" + df.format(it.endAtDatetime) + "  " +it.title!!)
                                }
                                /*終日の場合*/
                                else{
                                    scheduleMessageList.add("終日" + "  " +it.title!!)
                                }
                            }
                        }
                        /*改行区切りでリストの要素をscheduleMessageに代入*/
                        val scheduleMessage = scheduleMessageList.joinToString(separator = "\n")

                        val scheduleNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

                        /*スケジュール確認NotificationChannel設定*/
                        val scheduleChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT)
                        } else {
                            TODO("VERSION.SDK_INT < O")
                        }
                        scheduleChannel.description = title.toString()
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
                                    .setStyle(NotificationCompat.BigTextStyle()
                                    .setBigContentTitle(title)
                                    .bigText(scheduleMessage))
                                    .build()

                            /*通知出力*/
                            scheduleNotificationManager.notify(1, notification)

                        }
                    }

                    override fun onDataNotAvailable() {}
                }
        )
    }
}