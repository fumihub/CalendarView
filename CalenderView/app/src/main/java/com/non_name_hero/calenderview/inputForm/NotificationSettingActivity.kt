package com.non_name_hero.calenderview.inputForm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.non_name_hero.calenderview.R

class NotificationSettingActivity  /*コンストラクタ*/
    : AppCompatActivity() {

    private lateinit var scheduleSpinner: Spinner              /*スケジュール通知設定スピナー*/
    private lateinit var balanceSpinner: Spinner               /*入出金通知設定スピナー*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*初期設定***************************************/
        setContentView(R.layout.notification_setting)
        val myToolbar = findViewById<View>(R.id.notificationSettingToolbar) as Toolbar
        setSupportActionBar(myToolbar)
        scheduleSpinner = findViewById(R.id.scheduleSpinner)
        balanceSpinner = findViewById(R.id.balanceSpinner)
        /************************************************/

        /*スケジュール通知設定スピナー設定************************/

        /************************************************/

        /*入出金通知設定スピナー設定*************************/

        /************************************************/
    }

}