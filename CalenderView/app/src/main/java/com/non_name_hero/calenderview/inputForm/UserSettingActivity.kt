package com.non_name_hero.calenderview.inputForm

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.non_name_hero.calenderview.R
import java.util.*

class UserSettingActivity  /*コンストラクタ*/
    : AppCompatActivity() {

    private lateinit var mBirthdayAtDatetime: Calendar  /*初期表示使用日付*/

    private lateinit var userId: EditText               /*ユーザーID設定*/
    private lateinit var password: EditText             /*パスワード設定*/
    private lateinit var year: EditText                 /*生年月日　年*/
    private lateinit var month: EditText                /*生年月日　月*/
    private lateinit var day: EditText                  /*生年月日　日*/

    private lateinit var cancelButton: Button           /*キャンセルボタン*/
    private lateinit var doneButton: Button             /*保存ボタン*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*初期設定***************************************/
        setContentView(R.layout.user_setting)
        val myToolbar = findViewById<View>(R.id.userSettingToolbar) as Toolbar
        setSupportActionBar(myToolbar)
        mBirthdayAtDatetime = Calendar.getInstance()
        userId = findViewById(R.id.userId)
        password = findViewById(R.id.password)
        year = findViewById(R.id.year)
        month = findViewById(R.id.month)
        day = findViewById(R.id.day)
        cancelButton = findViewById(R.id.cancelButton)
        doneButton = findViewById(R.id.doneButton)
        /************************************************/

        /*ユーザーID設定***********************************/
        /*TODO FireBaseからユーザーIDを取得する*/
//        userId.hint =
        /************************************************/

        /*パスワード設定***********************************/
        /*TODO FireBaseからパスワードを取得する*/
//        password.hint =
        /************************************************/

        /*誕生日設定EditTextが押されたとき******************/
        /*TODO 設定された誕生日をSharedPreferenceに登録*/
        /*********************************************/

        /*完了ボタンが押されたとき*******************/
        doneButton.setOnClickListener {
            /*保存処理を実行*/
            /*設定画面に遷移*/
            /*TODO ユーザーID、パスワードの変更内容をFireBaseに反映
            *  　　　誕生日をSharedPreferenceに登録*/
//            mInputPresenter.saveBalance(price.text.toString().toLong(), balanceCategoryId, mUsedAtDatetime.time, title.text.toString())
        }
        /*********************************************/

        /*キャンセルボタンが押されたとき******************/
        cancelButton.setOnClickListener {
            /*カレンダー表示画面に遷移*/
            finish()
        }
        /*********************************************/

    }

}