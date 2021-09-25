package com.non_name_hero.calenderview.inputForm


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.data.source.ScheduleDataSource
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.utils.Injection
import android.content.Context
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.*


class UserCreateActivity  /*コンストラクタ*/
    : AppCompatActivity() {

    private lateinit var repository: ScheduleRepository     /**/

    private lateinit var mailAddress: EditText               /*メールアドレス*/
    private lateinit var password: EditText                  /*パスワード*/
    private lateinit var passwordCheck: EditText             /*確認用パスワード*/

    private lateinit var cancelButton: Button               /*キャンセルボタン*/
    private lateinit var createButton: Button               /*新規作成ボタン*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = Injection.provideScheduleRepository(applicationContext)
        setContentView(R.layout.user_create)
        val myToolbar = findViewById<View>(R.id.userCreateToolbar) as Toolbar
        setSupportActionBar(myToolbar)

        /*入力画面表示*************************************/
        mailAddress = findViewById(R.id.mailAddress)
        password = findViewById(R.id.password)
        passwordCheck = findViewById(R.id.passwordCheck)
        cancelButton = findViewById(R.id.cancelButton)
        createButton = findViewById(R.id.createButton)
        /**************************************************/

        /*初期化処理*****************************************/
        /*EditTextのEnterキーが押された時の処理*/
        setListener(mailAddress)
        setListener(password)
        setListener(passwordCheck)
        /**************************************************/

        /*新規作成ボタンが押されたとき*******************/
        createButton.setOnClickListener {
            /*新規ユーザー作成処理*/

            /*エラー確認*****************************/
            /*メールアドレスの入力確認*/
            var successFlag = if (textEmptyCheck(mailAddress, "メールアドレスを入力してください！")) {
                /*パスワードの入力確認*/
                if (textEmptyCheck(password, "パスワードを入力してください！")) {
                    /*パスワード確認用の入力確認*/
                    if (textEmptyCheck(passwordCheck, "パスワード確認用を入力してください！")) {
                        /*パスワードの入力が８文字以上か確認*/
                        if (textLengthCheck(password, "パスワードは８文字以上で入力してください！")) {
                            /*パスワード確認用の入力が８文字以上か確認*/
                            if (textLengthCheck(passwordCheck, "パスワード確認用は８文字以上で入力してください！")) {
                                /*パスワードと確認用パスワードの値が一致確認*/
                                if (passwordCheck.text.toString() == password.text.toString()) {
                                    true
                                }
                                /*パスワードと確認用パスワードの値が一致しない場合*/
                                else {
                                    /*エラー出力*/
                                    outputToast("パスワードと確認用パスワードが一致しません！")
                                    false
                                }
                            }
                            /*パスワード確認用の入力が８文字以上の場合*/
                            else {
                                false
                            }
                        }
                        /*パスワードの入力が８文字以上の場合*/
                        else {
                            false
                        }
                    /*パスワード確認用の入力がない場合*/
                    } else {
                        false
                    }
                /*パスワードの入力がない場合*/
                } else {
                    false
                }
            /*メールアドレスの入力がない場合*/
            } else {
                false
            }
            /****************************************/

            /*ユーザー作成処理**************************/
            if (successFlag) {
                repository.setUserInfo(mailAddress.text.toString(), password.text.toString(), object : ScheduleDataSource.SaveUserInfoCallback {
                    override fun onUserInfoSaved(existFlag: Boolean) {
                        if (existFlag) {
                            /*ユーザーの作成に成功した場合*/
                            /*ログイン画面に遷移*/
                            outputToast("ユーザーの作成に成功しました。")
                            returnLoginActivity()
                        }
                        else {
                            /*エラー出力*/
                            outputToast("メールアドレスがすでに存在します。")
                            outputToast("ユーザーの作成に失敗しました。")
                        }

                    }
                    /*ユーザーの作成に失敗した場合*/
                    override fun onDataNotAvailable() {
                        /*エラー出力*/
                        outputToast("ユーザーの作成に失敗しました。")
                    }
                })
            }
            /****************************************/

        }
        /*********************************************/

        /*キャンセルボタンが押されたとき******************/
        cancelButton.setOnClickListener {
            /*ログイン画面に遷移*/
            finish()
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

    /*ログイン画面遷移関数****************************/
    private fun returnLoginActivity() {
        val intentOut = Intent(this, LoginActivity::class.java)
        /*ユーザーIDを遷移先へreturn*/
        intentOut.putExtra("mailAddress", mailAddress.text.toString())
        /*パスワードを遷移先へreturn*/
        intentOut.putExtra("password", password.text.toString())
        setResult(RESULT_OK, intentOut)

        /*ログイン画面に遷移*/
        finish()
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