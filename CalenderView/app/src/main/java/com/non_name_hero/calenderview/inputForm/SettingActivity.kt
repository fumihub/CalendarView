package com.non_name_hero.calenderview.inputForm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.non_name_hero.calenderview.R

class SettingActivity  /*コンストラクタ*/
    : AppCompatActivity() {

    private lateinit var userIcon: ImageButton                   /*ユーザー設定アイコンボタン*/
    private lateinit var userSettingButton: Button               /*ユーザー設定画面遷移ボタン*/
    private lateinit var userSettingButton2: Button              /*ユーザー設定画面遷移ボタン2*/
    private lateinit var notificationIcon: ImageButton           /*通知設定アイコンボタン*/
    private lateinit var notificationSettingButton: Button       /*ユーザー設定画面遷移ボタン*/
    private lateinit var notificationSettingButton2: Button      /*通知設定画面遷移ボタン2*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*初期設定***************************************/
        setContentView(R.layout.setting)
        val myToolbar = findViewById<View>(R.id.settingToolbar) as Toolbar
        setSupportActionBar(myToolbar)
        userIcon = findViewById(R.id.userIcon)
        userSettingButton = findViewById(R.id.userSettingButton)
        userSettingButton2 = findViewById(R.id.userSettingButton2)
        notificationIcon = findViewById(R.id.notificationIcon)
        notificationSettingButton = findViewById(R.id.notificationSettingButton)
        notificationSettingButton2 = findViewById(R.id.notificationSettingButton2)
        /************************************************/

        /*ユーザー設定アイコンが押されたとき************************/
        userIcon.setOnClickListener {
            /*ユーザー設定画面に遷移*/
            goUserSettingActivity()
        }
        userSettingButton.setOnClickListener {
            /*ユーザー設定画面に遷移*/
            goUserSettingActivity()
        }
        userSettingButton2.setOnClickListener {
            /*ユーザー設定画面に遷移*/
            goUserSettingActivity()
        }
        /************************************************/

        /*通知設定アイコンが押されたとき************************/
        notificationIcon.setOnClickListener {
            /*通知設定画面に遷移*/
            goNotificationSettingActivity()
        }
        notificationSettingButton.setOnClickListener {
            /*通知設定画面に遷移*/
            goNotificationSettingActivity()
        }
        notificationSettingButton2.setOnClickListener {
            /*通知設定画面に遷移*/
            goNotificationSettingActivity()
        }
        /************************************************/
    }

    /*ユーザー設定画面遷移関数************************************/
    private fun goUserSettingActivity() {
        /*ユーザー設定画面遷移用intent*/
        val intentUserSettingActivity = Intent(this, UserSettingActivity::class.java)
        startActivityForResult(intentUserSettingActivity, REQUEST_CODE)
    }
    /************************************************/

    /*通知画面遷移関数************************************/
    private fun goNotificationSettingActivity() {
        /*通知設定画面遷移用intent*/
        val intentNotificationSettingActivity = Intent(this, NotificationSettingActivity::class.java)
        startActivityForResult(intentNotificationSettingActivity, REQUEST_CODE)
    }
    /************************************************/

    /*定数定義****************************************/
    companion object {
        private const val REQUEST_CODE = 1
    }

    /************************************************/

}