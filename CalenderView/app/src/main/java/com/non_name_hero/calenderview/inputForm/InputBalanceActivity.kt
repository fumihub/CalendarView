package com.non_name_hero.calenderview.inputForm

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.utils.Injection
import java.util.*

class InputBalanceActivity  /*コンストラクタ*/
    : AppCompatActivity() {

    private lateinit var repository: ScheduleRepository     /**/

    private lateinit var mUsedAtDatetime: Calendar         /*初期表示使用日付*/

    private lateinit var price: EditText                    /*金額入力*/
    private lateinit var categoryButton: EditText           /*カテゴリーボタン(アイコン用)*/
    private lateinit var categorySelectButton: EditText     /*カテゴリーセレクトボタン(カテゴリー名用)*/
    private lateinit var usedDate: EditText                 /*使用日付*/
    private lateinit var title: EditText                    /*内容*/

    private lateinit var cancelButton: Button               /*キャンセルボタン*/
    private lateinit var doneButton: Button                 /*保存ボタン*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = Injection.provideScheduleRepository(applicationContext)
        setContentView(R.layout.input_balance_main)
        val myToolbar = findViewById<View>(R.id.inputBalanceToolbar) as Toolbar
        setSupportActionBar(myToolbar)

        /*入力画面表示*************************************/
        /*カレンダーセルのボタンが押された場合*/
        price = findViewById(R.id.price)
        categoryButton = findViewById(R.id.categoryButton)
        categorySelectButton = findViewById(R.id.categorySelectButton)
        usedDate = findViewById(R.id.usedDate)
        title = findViewById(R.id.title)
        cancelButton = findViewById(R.id.cancelButton)
        doneButton = findViewById(R.id.doneButton)
        /**************************************************/

        /*初期値を設定********************************/
        val intentIn = intent
        val year:Int = intentIn.getIntExtra("year", 0)
        val month:Int = intentIn.getIntExtra("month", 0)
        val day:Int = intentIn.getIntExtra("day", 0)
        usedDate.setText("$month/$day")
        /*********************************************/

        /*カテゴリーボタンが押されたとき*****************/
        categoryButton.setOnClickListener {
            /*カテゴリー選択画面へ遷移*/
            goCategorySelectActivity()
        }
        categorySelectButton.setOnClickListener {
            /*カテゴリー選択画面へ遷移*/
            goCategorySelectActivity()
        }
        /*********************************************/

        /*使用日付日時EditTextが押されたとき*************/
        usedDate.setOnClickListener {
            /*TODO intent渡す側(CalendarPageFragment)を作成*/
            val intentIn = intent
            val year:Int = intentIn.getIntExtra("year", 0)
            val month:Int = intentIn.getIntExtra("month", 0)
            val day:Int = intentIn.getIntExtra("day", 0)
            /*DatePickerDialogインスタンスを取得*/
            val usedDatePickerDialog = DatePickerDialog(
                    this@InputBalanceActivity,
                    { _, year, month, dayOfMonth ->
                        /*setした日付を取得して表示*/
                        usedDate.setText(String.format("%02d / %02d", month + 1, dayOfMonth))
                        /*Calendarオブジェクトを作成*/
                        mUsedAtDatetime.set(year, month, dayOfMonth)
                    },
                    year,
                    /*monthは0が1月のため-1する必要がある*/
                    month - 1,
                    day
            )

            /*dialogを表示*/
            usedDatePickerDialog.show()
        }
        /*********************************************/

        /*完了ボタンが押されたとき*******************/
        doneButton.setOnClickListener {
            /*TODO 保存処理を実行*/

            /*カレンダー表示画面に遷移*/
        }
        /*********************************************/

        /*キャンセルボタンが押されたとき******************/
        cancelButton.setOnClickListener {
            /*カレンダー表示画面に遷移*/
            finish()
        }
        /*********************************************/


    }

    /*カテゴリー選択画面遷移関数*********************/
    private fun goCategorySelectActivity() {
        /*カテゴリー選択画面遷移用intent*/
        /*TODO カテゴリー選択画面作成*/
        val intentOut = Intent(this, ColorSelectActivity::class.java)
        /*戻り値を設定して色選択画面に遷移*/
        startActivityForResult(intentOut, InputBalanceActivity.REQUEST_CODE)
    }
    /************************************************/

    /*定数定義****************************************/
    companion object {
        private const val REQUEST_CODE = 1
    }
    /************************************************/
}