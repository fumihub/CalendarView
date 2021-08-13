package com.non_name_hero.calenderview.inputForm


import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.data.CategoryData
import com.non_name_hero.calenderview.data.source.ScheduleDataSource
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.utils.Injection
import com.non_name_hero.calenderview.inputForm.InputContract.Presenter
import java.util.*


class InputBalanceActivity  /*コンストラクタ*/
    : AppCompatActivity(), InputContract.View {

    private lateinit var mInputPresenter: Presenter         /**/

    private lateinit var repository: ScheduleRepository     /**/

    private lateinit var mUsedAtDatetime: Calendar         /*初期表示使用日付*/

    private lateinit var price: EditText                    /*金額入力*/
    private lateinit var priceText: TextView                /*金額入力*/
    private lateinit var usedDate: EditText                 /*使用日付*/
    private lateinit var title: EditText                    /*内容*/

    private lateinit var incomeButton: Button               /*収入ボタン*/
    private lateinit var costButton: Button                 /*費用ボタン*/
    private lateinit var categoryIconButton: ImageButton    /*カテゴリーボタン(アイコン用)*/
    private lateinit var categoryButton: Button             /*カテゴリーセレクトボタン(カテゴリー名用)*/
    private lateinit var cancelButton: Button               /*キャンセルボタン*/
    private lateinit var doneButton: Button                 /*保存ボタン*/

    private var categoryId = 1                               /*カテゴリID*/
    private var balanceCategoryId = 21                       /*サブカテゴリID*/

    private var topZeroJudgeFlag:Boolean = false            /*先頭0判定フラグ*/
    private var balanceFlag:Boolean = false                 /*収入費用フラグ*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = Injection.provideScheduleRepository(applicationContext)
        setContentView(R.layout.input_balance_main)
        val myToolbar = findViewById<View>(R.id.inputBalanceToolbar) as Toolbar
        setSupportActionBar(myToolbar)
        InputPresenter(this, Injection.provideScheduleRepository(applicationContext))

        /*入力画面表示*************************************/
        /*カレンダーセルのボタンが押された場合*/
        incomeButton = findViewById(R.id.incomeButton)
        costButton = findViewById(R.id.costButton)
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
        incomeButton.setBackgroundColor(0)
        /*********************************************/

        /*収入ボタンが押されたとき*************************/
        incomeButton.setOnClickListener {
            /*収入費用フラグON*/
            balanceFlag = true
            /*収入ボタンバッググランドカラーをredColor3に設定*/
            incomeButton.setBackgroundColor(-16728065)
            /*費用ボタンバッググランドカラーを透明に設定*/
            costButton.setBackgroundColor(0)
        }
        /*********************************************/
        /*費用ボタンが押されたとき************************/
        costButton.setOnClickListener {
            /*収入費用フラグOFF*/
            balanceFlag = false
            /*費用ボタンバッググランドカラーをblueColor3に設定*/
            costButton.setBackgroundColor(-1027705)
            /*収入ボタンバッググランドカラーを透明に設定*/
            incomeButton.setBackgroundColor(0)
        }
        /*********************************************/

        /*金額が入力されたとき************************/
        /*金額入力テキストから離れた時*/
        price.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                price.setText("")
            }
        }
        /*クリックされたとき¥0に戻す*/
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
            /*保存処理を実行*/
            /*カレンダー表示画面に遷移*/
            mInputPresenter.saveBalance(price.text.toString().toLong(), balanceCategoryId, mUsedAtDatetime.time, title.text.toString())
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
        /*収入の場合*/
        if (balanceFlag) {
            /*サブカテゴリー選択画面遷移用intent*/
            val intentSubCategorySelect = Intent(this, SubCategorySelectActivity::class.java)
            /*カテゴリIDを引数として渡す*/
            intentSubCategorySelect.putExtra("CategoryID", categoryId)
            /*戻り値を設定して色選択画面に遷移*/
            startActivityForResult(intentSubCategorySelect, REQUEST_CODE)
        }
        /*費用の場合*/
        else {
            /*カテゴリー選択画面遷移用intent*/
            val intentCategorySelect = Intent(this, CategorySelectActivity::class.java)
            /*戻り値を設定して色選択画面に遷移*/
            startActivityForResult(intentCategorySelect, REQUEST_CODE)
        }
    }
    /************************************************/

    /*SubCategorySelectActivityからbalanceCategoryIdをもらって、InputBalanceActivityに渡す関数*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE -> when (resultCode) {
                RESULT_OK -> {
                    /*バランスカテゴリID受け取り*/
                    balanceCategoryId = data!!.getIntExtra("BalanceCategoryId", 21)
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

    /*inputBalanceActivityを終了させる関数**************/
    override fun finishInput() {
        finish()
    }
    /************************************************/

    /*Presenterをsetする関数*************************/
    override fun setPresenter(presenter: Presenter?) {
        mInputPresenter = presenter!!
    }
    /************************************************/
}