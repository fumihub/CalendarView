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
import java.text.SimpleDateFormat
import java.util.*
import com.non_name_hero.calenderview.calendar.MainActivity
import com.non_name_hero.calenderview.inputForm.InputBalanceActivity


class AlarmNotification : BroadcastReceiver() {

    /*データを受信した*/
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmBroadcastReceiver", "onReceive() pid=" + Process.myPid())
        val requestCode = intent.getIntExtra("RequestCode", 0)
        val _intent = Intent(context, InputBalanceActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, requestCode, _intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val channelId = "default"
        val title = context.getString(R.string.app_name)
        val currentTime = System.currentTimeMillis()

        /*メッセージ(家計簿入力忘れ防止)*/
        val message = "入力忘れの入出金はございませんか？"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        /*Notification　Channel 設定*/
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        channel.description = message
        channel.enableVibration(true)
        channel.canShowBadge()
        channel.enableLights(true)
        channel.lightColor = Color.BLUE
        /*the channel appears on the lockscreen*/
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        channel.setSound(defaultSoundUri, null)
        channel.setShowBadge(true)
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel)
            val notification = Notification.Builder(context, channelId)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.pig_icon)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .build()

            /*通知出力*/
            notificationManager.notify(R.string.app_name, notification)
        }
    }
}