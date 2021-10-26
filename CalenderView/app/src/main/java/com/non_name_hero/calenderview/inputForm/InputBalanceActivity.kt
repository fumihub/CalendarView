package com.non_name_hero.calenderview.inputForm


import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.data.CategoryData
import com.non_name_hero.calenderview.data.source.ScheduleDataSource
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.utils.Injection
import com.non_name_hero.calenderview.inputForm.InputContract.Presenter
import com.non_name_hero.calenderview.utils.PigLeadUtils
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadBalanceCategoryDeleteDialog
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadDialogBase
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadDialogFragment
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadRepeatCheckDialog
import java.util.*


class InputBalanceActivity  /*コンストラクタ*/
    : AppCompatActivity(), InputContract.View, PigLeadRepeatCheckDialog {

    private lateinit var mInputPresenter: Presenter             /**/

    private lateinit var repository: ScheduleRepository         /**/

    private lateinit var mUsedAtDatetime: Calendar              /*初期表示使用日付*/

    private lateinit var price: EditText                        /*金額入力*/
    private lateinit var priceText: TextView                    /*金額入力*/
    private lateinit var usedDate: EditText                     /*使用日付*/
    private lateinit var title: EditText                        /*内容*/

    private lateinit var incomeButton: Button                   /*収入ボタン*/
    private lateinit var costButton: Button                     /*費用ボタン*/
    private lateinit var categoryIconButton: ImageButton        /*カテゴリーボタン(アイコン用)*/
    private lateinit var categoryButton: Button                 /*カテゴリーセレクトボタン(カテゴリー名用)*/
    private lateinit var cancelButton: Button                   /*キャンセルボタン*/
    private lateinit var doneButton: Button                     /*保存ボタン*/

    private var repeatDialog: PigLeadRepeatCheckDialog? = null /**/

    private var categoryId = 1                                 /*カテゴリID(デフォルト「収入」)*/
    private var balanceCategoryId = 29                          /*サブカテゴリID(デフォルト「未分類」)*/

    private var topZeroJudgeFlag:Boolean = false                /*先頭0判定フラグ*/
    private var balanceFlag:Boolean = false                     /*収入費用フラグ*/
    private var repeatFlag:Boolean = false                     /*再入力フラグ*/

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
        val year = intentIn.getIntExtra("year", 0)
        val month = intentIn.getIntExtra("month", 0)
        val day = intentIn.getIntExtra("day", 0)
        usedDate.setText(String.format("%04d/%02d/%02d", year, month, day))
        mUsedAtDatetime = Calendar.getInstance()
        mUsedAtDatetime.set(year, month - 1, day)
        incomeButton.setBackgroundColor(0)
        repeatDialog = this
        /*********************************************/

        /*収入ボタンが押されたとき*************************/
        incomeButton.setOnClickListener {
            /*収入費用フラグON*/
            balanceFlag = true
            /*収入ボタンバッググランドカラーをredColor3に設定*/
            incomeButton.setBackgroundColor(-16728065)
            /*費用ボタンバッググランドカラーを透明に設定*/
            costButton.setBackgroundColor(0)

            /*アイコンボタン設定(デフォルトで「給与」)*/
            val id: Int = getResources().getIdentifier("income_icon", "drawable", getPackageName())
            categoryIconButton.setImageResource(id)
            categoryIconButton.setBackgroundColor(-16728065)
            balanceCategoryId = INCOME_DEFAULT_ID
            /*リストのカテゴリーボタンにテキストをセット*/
            categoryButton.text = INCOME_DEFAULT_TEXT
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

            /*アイコンボタン設定(デフォルトで「未分類」)*/
            val id: Int = getResources().getIdentifier("unclassified_icon", "drawable", getPackageName())
            categoryIconButton.setImageResource(id)
            categoryIconButton.setBackgroundColor(0)
            balanceCategoryId = EXPENSES_DEFAULT_ID
            /*リストのカテゴリーボタンにテキストをセット*/
            categoryButton.text = EXPENSES_DEFAULT_TEXT
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
        /*****************************************/

        price.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var priceStr: String = ""
                topZeroJudgeFlag = true

                s?.asIterable()?.forEach { char ->
                    run {
                        if (char.toString() == "" && topZeroJudgeFlag) {

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
            /*DatePickerDialogインスタンスを取得*/
            val usedDatePickerDialog = DatePickerDialog(
                    this@InputBalanceActivity,
                    /*DatePickerで選択された日付*/
                    { _, year, month, dayOfMonth ->
                        /*setした日付を取得して表示*/
                        usedDate.setText(String.format("%04d/%02d/%02d", year, month + 1, dayOfMonth))
                        /*Calendarオブジェクトを作成*/
                        mUsedAtDatetime.set(year, month, dayOfMonth)
                    },
                    /*DatePickerに初期表示される日付*/
                    mUsedAtDatetime.get(Calendar.YEAR),
                    mUsedAtDatetime.get(Calendar.MONTH),
                    mUsedAtDatetime.get(Calendar.DATE)
            )

            /*dialogを表示*/
            usedDatePickerDialog.show()
        }
        /*********************************************/

        /*完了ボタンが押されたとき*******************/
        doneButton.setOnClickListener {
            if (errorCheck()) {
                /*保存処理を実行*/
                /*カレンダー表示画面に遷移*/
                mInputPresenter.saveBalance(price.text.toString().toLong(), balanceCategoryId, mUsedAtDatetime.time, title.text.toString())
                /*トースト出力*/
                outputToast("家計簿を登録しました。")
            }
        }
        /*********************************************/

        /*キャンセルボタンが押されたとき******************/
        cancelButton.setOnClickListener {
            /*カレンダー表示画面に遷移*/
            finish()
        }
        /*********************************************/

    }

    /*エラーチェック関数*********************************/
    private fun errorCheck(): Boolean {
        /*金額が入力されているかチェック*/
        /*金額が入力されている場合*/
        if (price.text.toString() != "") {

            /*12:00に設定*/
            mUsedAtDatetime.set(Calendar.HOUR_OF_DAY, 12)
            mUsedAtDatetime.set(Calendar.MINUTE, 0)
            return true

        }
        /*金額が入力されていない場合*/
        else {
            /*トースト出力*/
            outputToast("金額を入力してください。")
            return false
        }
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

    /**
     * 再入力確認用ダイアログを設定
     *
     * @param price 金額の文字列(収入なら先頭に「+」付きの青文字、費用なら｢-｣付きの赤文字)
     * @param balanceCategoryName カテゴリー名
     * @param mUsedAtDatetime 日付の文字列(YYYY/MM/DD)
     * @param title 内容
     * @param callback ダイアログのボタン押下時の処理
     * @return dialog DialogFragmentのオブジェクト
     */
    @RequiresApi(Build.VERSION_CODES.N)
    override fun getRepeatDialog(price: String, balanceCategoryName: String, mUsedAtDatetime: String, title: String, callback: PigLeadDialogBase.DialogCallback): PigLeadDialogFragment? {
        /*表示させるメッセージの定義*/
        val html = """
                <html>
                |<h1><u><font color='#FF4081'>再入力確認</font></u></h1>
                |<br>
                |日付：<b>${mUsedAtDatetime}</b><br>
                |カテゴリー：<b>${balanceCategoryName}</b><br>
                |金額：<b>${price}</b><br>
                |出金元/入金先：<b></b><br>
                |内容：<b>${title}</b><br></html>""".trimMargin()
        val dialogMessage = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)

        val positiveBtnMessage = getString(R.string.repeat_balance_positive)
        val negativeBtnMessage = getString(R.string.repeat_balance_negative)
        /*AlertDialogを設定*/
        val dialog = PigLeadDialogFragment(this)
        dialog.setDialogMessage(dialogMessage)
                .setPositiveBtnMessage(positiveBtnMessage)
                .setNegativeBtnMessage(negativeBtnMessage)
                .setPositiveClickListener { dialog, which -> callback.onClickPositiveBtn() }
                .setNegativeClickListener { dialog, which -> callback.onClickNegativeBtn() }
        return dialog
    }

    /************************************************/

    /*再入力確認ダイアログ表示関数*********************/
    override fun showPigLeadDiaLog(dialog: PigLeadDialogFragment?) {
        /*AlertDialogを表示*/
        dialog?.show(supportFragmentManager, REPEAT_DIALOG_TAG)
    }
    /************************************************/

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
                    balanceCategoryId = data!!.getIntExtra("BalanceCategoryId", 29)
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

    /*inputBalanceActivityを終了させる関数**************/
    override fun finishInput() {

        /*再入力を確認するダイアログを作成***/
        val priceString =
                /*収入の場合、先頭に「+」付きの青文字*/
                if (balanceFlag){
                    "<font color='#00BFFF'>¥ +" + price.text.toString().toLong() + "</font>"
                }
                /*費用場合、先頭に｢-｣付きの赤文字*/
                else{
                    "<font color='#F16682'>¥ -" + price.text.toString().toLong() + "</font>"
                }
        val dialog = repeatDialog!!.getRepeatDialog(
                priceString,
                categoryButton.text.toString(),
                PigLeadUtils.formatYYYYMMDD.format(mUsedAtDatetime.time),
                title.text.toString(),
                object : PigLeadDialogBase.DialogCallback {
            override fun onClickPositiveBtn() {
                /*続けて入力*/
                /*一度画面閉じる*/
                finish()
                /*再入力するためにこのクラスをもう一度呼び出す*/
                val intentRepeat = Intent(this@InputBalanceActivity, this@InputBalanceActivity::class.java)
                intentRepeat.putExtra("year", mUsedAtDatetime.get(Calendar.YEAR))
                /*monthは0が1月のため+1する必要がある*/
                intentRepeat.putExtra("month", mUsedAtDatetime.get(Calendar.MONTH) + 1)
                intentRepeat.putExtra("day", mUsedAtDatetime.get(Calendar.DATE))
                startActivity(intentRepeat)
            }

            override fun onClickNegativeBtn() {
                /*完了*/
                finish()
            }
        })
        repeatDialog!!.showPigLeadDiaLog(dialog)
        /********************************/

    }
    /************************************************/

    /*Presenterをsetする関数*************************/
    override fun setPresenter(presenter: Presenter?) {
        mInputPresenter = presenter!!
    }
    /************************************************/
    
    /*定数定義****************************************/
    companion object {
        private const val REQUEST_CODE = 1
        private const val INCOME_DEFAULT_ID = 1
        private const val EXPENSES_DEFAULT_ID = 29
        private const val INCOME_DEFAULT_TEXT = "給与"
        private const val EXPENSES_DEFAULT_TEXT = "未分類"
        private const val REPEAT_DIALOG_TAG = "REPEAT_DIALOG"
    }
    /************************************************/
    
}
