package com.non_name_hero.calenderview.inputForm

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.non_name_hero.calenderview.R
import java.util.*

class UserSettingActivity  /*コンストラクタ*/
    : AppCompatActivity() {

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
        password = findViewById(R.id.password)
        year = findViewById(R.id.year)
        month = findViewById(R.id.month)
        day = findViewById(R.id.day)
        cancelButton = findViewById(R.id.cancelButton)
        doneButton = findViewById(R.id.doneButton)
        /************************************************/

        /*パスワード設定***********************************/
        /*TODO SharePreferenceからパスワードを取得する*/
//        password.hint =
        /***********************************************/

        /*誕生日設定*************************************/
        val prefs = getSharedPreferences("input_data", Context.MODE_PRIVATE)
        year.setText(prefs.getString("yearStr", ""))
        month.setText(prefs.getString("monthStr", ""))
        day.setText(prefs.getString("dayStr", ""))
        /**********************************************/

        /*完了ボタンが押されたとき*******************/
        doneButton.setOnClickListener {
            /*保存処理を実行*/
            /*TODO パスワードの変更内容をFireBase,SharedPreferenceに反映*/


            /*誕生日判定*************************************/
            val yearStr = year.text.toString()
            val monthStr = month.text.toString()
            val dayStr = day.text.toString()

            /*空白の場合*/
            if ((yearStr == "")
                    || (monthStr == "")
                    || (dayStr == "")) {
                /*SharedPreferenceに保存*/
                editSharedPreference(yearStr, monthStr,dayStr)
                /*エラー出力*/
                outputToast("誕生日は設定されませんでした。")
                /*SharedPreferenceに全て空白で保存*/
                editSharedPreference("", "", "")
                /*設定画面に遷移*/
                finish()
            }
            else {
                val yearInt = Integer.valueOf(yearStr)
                val monthInt = Integer.valueOf(monthStr)
                val dayInt = Integer.valueOf(dayStr)

                /*年の判定(1921~2021　100歳まで)*/
                /*SharedPreferenceに登録*/
                if ((1921 <= yearInt)
                        && (yearInt <= 2021)) {
                    /*月の判定(1~12)*/
                    /*SharedPreferenceに登録*/
                    if ((1 <= monthInt)
                            && (monthInt <= 12)) {
                        /*日の判定(1~31　(2,4,6,9,11月は1~30))*/
                        /*SharedPreferenceに登録*/
                        /*1,3,5,7,8,10,12月*/
                        if ((monthInt == 1)
                                || (monthInt == 3)
                                || (monthInt == 5)
                                || (monthInt == 7)
                                || (monthInt == 8)
                                || (monthInt == 10)
                                || (monthInt == 12)) {
                            if ((1 <= dayInt)
                                    && (dayInt <= 31)) {
                                /*SharedPreferenceに保存*/
                                editSharedPreference(yearStr, monthStr,dayStr)
                                /*設定完了出力*/
                                outputToast(yearStr+"年"+monthStr+"月"+dayStr+"日"+"で誕生日を設定しました。")
                                /*設定画面に遷移*/
                                finish()
                            }
                            /*エラーメッセージ表示(日)*/
                            else {
                                /*エラー出力*/
                                outputToast("dayには1~31までの数値を入力してください！")
                            }
                        }
                        /*1,3,5,7,8,10,12月*/
                        else if ((monthInt == 2)
                                || (monthInt == 4)
                                || (monthInt == 6)
                                || (monthInt == 9)
                                || (monthInt == 11)) {
                            if ((1 <= dayInt)
                                    && (dayInt <= 30)) {
                                /*SharedPreferenceに保存*/
                                editSharedPreference(yearStr, monthStr, dayStr)
                                /*設定完了出力*/
                                outputToast(yearStr+"年"+monthStr+"月"+dayStr+"日"+"で誕生日を設定しました。")
                                /*設定画面に遷移*/
                                finish()
                            }
                            /*エラーメッセージ表示(日)*/
                            else {
                                /*エラー出力*/
                                outputToast("dayには1~30までの数値を入力してください！")
                            }
                        }
                    }
                    /*エラーメッセージ表示(月)*/
                    else {
                        /*エラー出力*/
                        outputToast("monthには1~12までの数値を入力してください！")
                    }
                }
                /*エラーメッセージ表示(年)*/
                else {
                    /*エラー出力*/
                    outputToast("yearには1921~2021までの数値を入力してください！")
                }
            }
            /*********************************************/

        }
        /*********************************************/

        /*キャンセルボタンが押されたとき******************/
        cancelButton.setOnClickListener {
            /*設定画面に遷移*/
            finish()
        }
        /*********************************************/

    }

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

    /*SharedPreference保存関数************************/
    private fun editSharedPreference(yearStr: String, monthStr: String, dayStr: String) {
        val prefs = getSharedPreferences("input_data", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        /*SharedPreferenceにyearStrの値を保存*/
        editor.putString("yearStr", yearStr)
        /*非同期処理ならapply()、同期処理ならcommit()*/
        editor.apply()
        /*SharedPreferenceにmonthStrの値を保存*/
        editor.putString("monthStr", monthStr)
        /*非同期処理ならapply()、同期処理ならcommit()*/
        editor.apply()
        /*SharedPreferenceにdayIntの値を保存*/
        editor.putString("dayStr", dayStr)
        /*非同期処理ならapply()、同期処理ならcommit()*/
        editor.apply()
    }
    /************************************************/

}