package com.non_name_hero.calenderview.notification

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
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.inputForm.InputBalanceActivity
import com.non_name_hero.calenderview.utils.PigLeadUtils
import java.util.*

class BalanceNotification : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmBroadcastReceiver", "onReceive() pid=" + Process.myPid())
        val requestCode = intent.getIntExtra("RequestCode", 0)
        val balanceIntent = Intent(context, InputBalanceActivity::class.java)
        //入力画面に引数で年月日を渡す
        val calendar = Calendar.getInstance()
        val year: Int = Integer.valueOf(PigLeadUtils.yearFormat.format(calendar.time))
        val month: Int = Integer.valueOf(PigLeadUtils.monthFormat.format(calendar.time))
        val day: Int = Integer.valueOf(PigLeadUtils.dayFormat.format(calendar.time))
        balanceIntent.putExtra("year", year)
        balanceIntent.putExtra("month", month)
        balanceIntent.putExtra("day", day)
        val pendingBalanceIntent = PendingIntent.getActivity(context, requestCode, balanceIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val channelId = "default"
        val title = "Balance Check"

        /*メッセージ(家計簿入力忘れ防止)*/
        val balanceMessage = "入力忘れの入出金はございませんか？"
        val balanceNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        /*入出金確認用NotificationChannel設定*/
        val balanceChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        balanceChannel.description = balanceMessage
        balanceChannel.enableVibration(true)
        balanceChannel.canShowBadge()
        balanceChannel.enableLights(true)
        balanceChannel.lightColor = Color.BLUE
        /*the channel appears on the lockscreen*/
        balanceChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        balanceChannel.setSound(defaultSoundUri, null)
        balanceChannel.setShowBadge(true)
        if (balanceNotificationManager != null) {
            balanceNotificationManager.createNotificationChannel(balanceChannel)
            val notification = Notification.Builder(context, channelId)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.pig_icon)
                    .setContentText(balanceMessage)
                    .setAutoCancel(true)
                    .setContentIntent(pendingBalanceIntent)
                    .setWhen(System.currentTimeMillis())
                    .build()

            /*通知出力*/
            balanceNotificationManager.notify(2, notification)
        }
    }
}