package com.non_name_hero.calenderview.inputForm


import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.data.CategoryData
import com.non_name_hero.calenderview.data.source.ScheduleDataSource
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.utils.Injection
import com.non_name_hero.calenderview.inputForm.InputContract.Presenter
import java.util.*
import android.app.AlarmManager

import android.app.PendingIntent
import android.content.Context
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.non_name_hero.calenderview.calendar.MainActivity
import com.non_name_hero.calenderview.notification.AlarmNotification


class LoginActivity  /*コンストラクタ*/
    : AppCompatActivity() {

    private lateinit var repository: ScheduleRepository     /**/

    private lateinit var mailAddress: EditText               /*メールアドレス*/
    private lateinit var passwordEditText: EditText          /*パスワード*/

    private lateinit var cancelButton: Button               /*キャンセルボタン*/
    private lateinit var loginButton: Button                /*ログインボタン*/
    private lateinit var userCreateButton: Button           /*新規ユーザー作成ボタン*/

    private lateinit var checkBox: CheckBox                 /*次回入力省略チェックボックス*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        repository = Injection.provideScheduleRepository(applicationContext)
        setContentView(R.layout.login)
        val myToolbar = findViewById<View>(R.id.loginToolbar) as Toolbar
        setSupportActionBar(myToolbar)

        /*入力画面表示*************************************/
        mailAddress = findViewById(R.id.mailAddress)
        passwordEditText = findViewById(R.id.password)
        cancelButton = findViewById(R.id.cancelButton)
        loginButton = findViewById(R.id.loginButton)
        userCreateButton = findViewById(R.id.userCreateButton)
        checkBox = findViewById(R.id.checkbox)
        /**************************************************/

        /*以前にチェックボックスにチェックが入っていた場合**********/
        val prefs = getSharedPreferences("input_data", Context.MODE_PRIVATE)

        if (prefs.getBoolean("checkFlag", false)) {
            /*SharedPreferenceからユーザー情報を抽出*/
            mailAddress.setText(prefs.getString("mailAddress", ""))
            passwordEditText.setText(prefs.getString("password", ""))
        }
        /**************************************************/

        /*初期化処理*****************************************/
        /*EditTextのEnterキーが押された時の処理*/
        setListener(mailAddress)
        setListener(passwordEditText)
        /**************************************************/

        /*チェックボックスが押されたとき*******************/
        checkBox.setOnClickListener {
            val prefs = getSharedPreferences("input_data", Context.MODE_PRIVATE)
            val editor = prefs.edit()

            /*SharedPreferenceにcheckedの値を保存*/
            editor.putBoolean("checkFlag", checkBox.isChecked)
            /*非同期処理ならapply()、同期処理ならcommit()*/
            editor.apply()
        }
        /*********************************************/

        /*ログインボタンが押されたとき*******************/
        loginButton.setOnClickListener {

            /*エラー確認*****************************/
            /*メールアドレスの入力確認*/
            var successFlag = if (textEmptyCheck(mailAddress, "メールアドレスを入力してください！")) {
                /*パスワードの入力確認*/
                if (textEmptyCheck(passwordEditText, "パスワードを入力してください！")) {
                    /*パスワードの入力が８文字以上か確認*/
                    textLengthCheck(passwordEditText, "パスワードは８文字以上で入力してください！")
                /*パスワードの入力がない場合*/
                } else {
                    false
                }
            /*メールアドレスの入力がない場合*/
            } else {
                false
            }
            /****************************************/

            /*ログイン処理*****************************/
            if (successFlag) {
                repository.getUserInfo(mailAddress.text.toString(), object : ScheduleDataSource.GetUserInfoCallback {
                    override fun onUserInfoLoaded(password: String) {
                        /*パスワードが空文字の場合*/
                        if (password == "") {
                            /*エラー出力*/
                            outputToast("メールアドレスが存在しませんでした。")
                            outputToast("ログインに失敗しました。")
                        }
                        /*パスワードに文字列が入っている場合*/
                        else {
                            /*パスワードが一致する場合*/
                            if (password == passwordEditText.text.toString()) {
                                /*SharedPreferenceにメールアドレスとパスワードを登録*/
                                val prefs = getSharedPreferences("input_data", Context.MODE_PRIVATE)
                                val editor = prefs.edit()
                                /*SharedPreferenceにメールアドレスの値を保存*/
                                editor.putString("mailAddress", mailAddress.text.toString())
                                /*SharedPreferenceにパスワードの値を保存*/
                                editor.putString("password", passwordEditText.text.toString())
                                /*非同期処理ならapply()、同期処理ならcommit()*/
                                editor.apply()

                                /*カレンダー画面に遷移*/
                                outputToast("ログインに成功しました。")
                                goMainActivity()
                            }
                            /*パスワードに一致しない場合*/
                            else {
                                /*エラー出力*/
                                outputToast("パスワードが一致しませんでした。")
                                outputToast("ログインに失敗しました。")
                            }
                        }
                    }

                    override fun onDataNotAvailable() {
                        /*エラー出力*/
                        outputToast("メールアドレスの取得が失敗しました。")
                        outputToast("ログインに失敗しました。")
                    }
                })
            }
            /****************************************/

        }
        /*********************************************/

        /*キャンセルボタンが押されたとき******************/
        cancelButton.setOnClickListener {
            /*アプリ終了*/
            finish()
        }
        /*********************************************/

        /*新規ユーザー作成ボタンが押されたとき***************/
        userCreateButton.setOnClickListener {
            /*新規ユーザー作成画面に遷移*/
            goUserCreateActivity()
        }
        /*********************************************/

    }

    /*EditTextのEnterキーが押された時にキーボードを閉じる処理*/
    private fun setListener(editText: EditText) {
        editText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                val inputMethodManager = this?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                /*キーボード閉じる処理*/
                inputMethodManager.hideSoftInputFromWindow(v.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)

                true
            }
            false
        }
    }
    /************************************************/

    /*EditText入力チェック関数****************************/
    private fun textEmptyCheck(editText: EditText, str: String): Boolean {
        /*EditTextの入力がない場合*/
        if (editText.text.isEmpty()) {
            /*エラー出力*/
            outputToast(str)
            return false
        }
        return true
    }
    /************************************************/

    /*EditText入力の長さチェック関数****************************/
    private fun textLengthCheck(editText: EditText, str: String): Boolean {
        /*EditTextの入力が８文字以上の場合*/
        if (editText.text.length > 8) {
            /*エラー出力*/
            outputToast(str)
            return false
        }
        return true
    }
    /************************************************/

    /*カレンダー画面遷移関数*********************/
    private fun goMainActivity() {

        /*カレンダー画面遷移用intent*/
        val intentMain = Intent(this, MainActivity::class.java)
        /*戻り値を設定してカレンダー画面に遷移*/
        startActivity(intentMain)

    }
    /************************************************/

    /*新規ユーザー作成画面遷移関数*********************/
    private fun goUserCreateActivity() {

        /*新規ユーザー作成画面遷移用intent*/
        val intentUserCreate = Intent(this, UserCreateActivity::class.java)
        /*戻り値を設定して新規ユーザー作成画面に遷移*/
        startActivityForResult(intentUserCreate, REQUEST_CODE)

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

    /*UserCreateActivityからuserIdとpasswordをもらって、EditTextに反映する関数*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE -> when (resultCode) {
                RESULT_OK -> {
                    /*ユーザーID受け取り*/
                    mailAddress.setText(data!!.getStringExtra("mailAddress"))
                    /*パスワード受け取り*/
                    passwordEditText.setText(data!!.getStringExtra("password"))
                }
                RESULT_CANCELED -> {
                    /*キャンセルボタンを押して戻ってきたときの処理*/
                }
                else -> {
                    /*その他*/
                }
            }
            else -> {
            }
        }
    }

    /************************************************/


    /*定数定義****************************************/
    companion object {
        private const val REQUEST_CODE = 1
    }
    /************************************************/
}