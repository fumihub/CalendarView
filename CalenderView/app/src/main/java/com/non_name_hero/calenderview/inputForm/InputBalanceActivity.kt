package com.non_name_hero.calenderview.inputForm

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.data.CategoryData
import com.non_name_hero.calenderview.data.ScheduleGroup
import com.non_name_hero.calenderview.data.source.ScheduleDataSource
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.utils.Injection
import java.util.*

class InputBalanceActivity  /*コンストラクタ*/
    : AppCompatActivity() {

    private lateinit var repository: ScheduleRepository     /**/

    private lateinit var mUsedAtDatetime: Calendar         /*初期表示使用日付*/

    private lateinit var price: EditText                    /*金額入力*/
    private lateinit var priceText: TextView                /*金額入力*/
    private lateinit var usedDate: EditText                 /*使用日付*/
    private lateinit var title: EditText                    /*内容*/

    private lateinit var categoryIconButton: ImageButton    /*カテゴリーボタン(アイコン用)*/
    private lateinit var categoryButton: Button             /*カテゴリーセレクトボタン(カテゴリー名用)*/
    private lateinit var cancelButton: Button               /*キャンセルボタン*/
    private lateinit var doneButton: Button                 /*保存ボタン*/

    private var balanceCategoryId = 0                       /*サブカテゴリID*/

    private var topZeroJudgeFlag:Boolean = false            /*先頭0判定フラグ*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = Injection.provideScheduleRepository(applicationContext)
        setContentView(R.layout.input_balance_main)
        val myToolbar = findViewById<View>(R.id.inputBalanceToolbar) as Toolbar
        setSupportActionBar(myToolbar)

        /*入力画面表示*************************************/
        /*カレンダーセルのボタンが押された場合*/
        price = findViewById(R.id.price)
        priceText = findViewById(R.id.priceText)
        categoryIconButton = findViewById(R.id.categoryIconButton)
        categoryButton = findViewById(R.id.categoryButton)
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
        usedDate.setText(String.format("%02d / %02d", month, day))
        mUsedAtDatetime = Calendar.getInstance()
        mUsedAtDatetime.set(year, month - 1, day)
        /*********************************************/

        /*金額が入力されたとき************************/
        /*金額入力テキストから離れた時*/
        price.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                price.setText("")
            }
        }
        price.setOnClickListener {
            price.setText("")
        }
        /*******************************/

        price.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var priceStr: String = ""
                topZeroJudgeFlag = true

                s?.asIterable()?.forEach { char ->
                    run {
                        if (char.toString() == "0" && topZeroJudgeFlag) {

                        } else {
                            priceStr += char.toString()
                            topZeroJudgeFlag = false
                        }
                    }
                }
                if (priceStr.length <= 10) {
                    val price: Long =
                            if (priceStr == "") {
                                0
                            } else {
                                priceStr.toLong()
                            }
                    priceText.text = "¥" + String.format("%,d", price)
                } else {
                    price.setText(priceStr.substring(0, 10))
                    price.setSelection(price.text.length)
                }
            }

            override fun afterTextChanged(s: Editable?) {


            }
        })
        /*********************************************/

        /*カテゴリーボタンが押されたとき*****************/
        categoryIconButton.setOnClickListener {
            /*カテゴリー選択画面へ遷移*/
            goCategorySelectActivity()
        }
        categoryButton.setOnClickListener {
            /*カテゴリー選択画面へ遷移*/
            goCategorySelectActivity()
        }
        /*********************************************/

        /*使用日付日時EditTextが押されたとき*************/
        usedDate.setOnClickListener {
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
        val intentOut = Intent(this, CategorySelectActivity::class.java)
        /*戻り値を設定して色選択画面に遷移*/
        startActivityForResult(intentOut, InputBalanceActivity.REQUEST_CODE)
    }
    /************************************************/

    /*SubCategorySelectActivityからbalanceCategoryIdをもらって、InputBalanceActivityに渡す関数*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE -> when (resultCode) {
                RESULT_OK -> {
                    /*バランスカテゴリID受け取り*/
                    balanceCategoryId = data!!.getIntExtra("BalanceCategoryId", 1)
                    /*DBからbalanceCategoryIdをキーにその要素を取得*/
                    repository.getCategoryData(
                            balanceCategoryId,
                            object : ScheduleDataSource.GetCategoryDataCallback {
                                override fun onCategoryDataLoaded(categoryData: CategoryData) {
                                    /*アイコンボタン設定*/
                                    val id: Int = getResources().getIdentifier(categoryData.imgURL, "drawable", getPackageName())
                                    categoryIconButton.setImageResource(id)
                                    categoryIconButton.setBackgroundColor(categoryData.categoryColor)
                                    /*リストのカテゴリーボタンにテキストをセット*/
                                    categoryButton.text = categoryData.categoryName
                                }
                            }
                    )
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