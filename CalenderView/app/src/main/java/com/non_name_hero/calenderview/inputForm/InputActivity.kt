package com.non_name_hero.calenderview.inputForm

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.data.ScheduleGroup
import com.non_name_hero.calenderview.data.source.ScheduleDataSource.GetScheduleGroupCallback
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.inputForm.InputContract.Presenter
import com.non_name_hero.calenderview.utils.Injection
import java.util.*

class InputActivity  /*コンストラクタ*/
    : AppCompatActivity(), InputContract.View {

    private lateinit var mInputPresenter: Presenter         /**/

    private lateinit var repository: ScheduleRepository     /**/

    private lateinit var mStartAtDatetime: Calendar         /*初期表示開始日付*/
    private lateinit var mEndAtDatetime: Calendar           /*初期表示終了日付*/

    private lateinit var title: EditText                    /*スケジュールタイトル*/
    private lateinit var startDate: EditText                /*スケジュール開始日付*/
    private lateinit var endDate: EditText                  /*スケジュール終了日付*/
    private lateinit var startTime: EditText                /*スケジュール開始時間*/
    private lateinit var endTime: EditText                  /*スケジュール終了時間*/
    private lateinit var timeArrow: TextView                /*時間用矢印*/
    private lateinit var place: TextView                    /*スケジュール場所*/
    private lateinit var memo: EditText                     /*スケジュールメモ*/
    private lateinit var timeButton: Button                 /*時間入力表示ボタン*/
    private lateinit var color1: Button                     /*ColorSelectActivity遷移ボタン1*/
    private lateinit var color2: Button                     /*ColorSelectActivity遷移ボタン1*/
    private lateinit var mapCheckButton: ImageButton        /*GoogleMap遷移ボタン*/
    private lateinit var detailButton: Button               /*詳細入力表示ボタン*/
    private lateinit var cancelButton: Button               /*キャンセルボタン*/
    private lateinit var doneButton: Button                 /*保存ボタン*/

    private var colorNumber = 0                             /*色番号(0~48)*/
    private var mGroupId = 0                                /*色グループID(0~)*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = Injection.provideScheduleRepository(applicationContext)
        setContentView(R.layout.input_main)
        val myToolbar = findViewById<View>(R.id.inputToolbar) as Toolbar
        setSupportActionBar(myToolbar)
        InputPresenter(this, Injection.provideScheduleRepository(applicationContext))

        /*カレンダー初期値用*/
        val intentIn = intent
        val year:Int = intentIn.getIntExtra("year", 0)
        val month:Int = intentIn.getIntExtra("month",0)
        val day:Int = intentIn.getIntExtra("day",0)
        /*グループIDの初期値設定*/
        mGroupId = 1


        /*入力画面表示***********************************/
        /*カレンダーセルのボタンが押された場合*/
        title = findViewById(R.id.title)
        startDate = findViewById(R.id.startDate)
        endDate = findViewById(R.id.endDate)
        timeButton = findViewById(R.id.timeButton)
        startTime = findViewById(R.id.startTime)
        endTime = findViewById(R.id.endTime)
        timeArrow = findViewById(R.id.timeArrow)
        color1 = findViewById(R.id.colorButton1)
        color2 = findViewById(R.id.colorButton2)
        detailButton = findViewById(R.id.detailButton)
        mapCheckButton = findViewById(R.id.mapCheckButton)
        detailButton = findViewById(R.id.detailButton)
        detailButton = findViewById(R.id.detailButton)
        place = findViewById(R.id.place)
        memo = findViewById(R.id.memo)
        cancelButton = findViewById(R.id.cancelButton)
        doneButton = findViewById(R.id.doneButton)

        /*最初表示************************************/
        title.visibility = View.VISIBLE
        startDate.visibility = View.VISIBLE
        endDate.visibility = View.VISIBLE
        timeButton.visibility = View.VISIBLE
        color1.visibility = View.VISIBLE
        color2.visibility = View.VISIBLE
        detailButton.visibility = View.VISIBLE
        cancelButton.visibility = View.VISIBLE
        doneButton.visibility = View.VISIBLE
        /*********************************************/

        /*最初非表示**********************************/
        startTime.visibility = View.GONE
        endTime.visibility = View.GONE
        timeArrow.visibility = View.GONE
        mapCheckButton.visibility = View.GONE
        place.visibility = View.GONE
        memo.visibility = View.GONE
        /*********************************************/

        /*初期表示日付取得****************************/
        mStartAtDatetime = Calendar.getInstance()
        mStartAtDatetime.set(year, month - 1, day)
        mEndAtDatetime = Calendar.getInstance()
        mEndAtDatetime.set(year, month - 1, day)
        /*********************************************/

        /*タイトルEditTextが押されたとき*************/
        /*title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //フォーカスを当てる
                title.setFocusable(true);
                title.setFocusableInTouchMode(true);
            }
        });*/
        /*********************************************/

        /*初期値を設定********************************/
        startDate.setText(String.format("%02d / %02d", month, day))
        endDate.setText(String.format("%02d / %02d", month, day))
        /*********************************************/

        /*開始日時EditTextが押されたとき*************/
        startDate.setOnClickListener {
            val intentIn = intent
            val year:Int = intentIn.getIntExtra("year", 0)
            val month:Int = intentIn.getIntExtra("month", 0)
            val day:Int = intentIn.getIntExtra("day", 0)
            /*DatePickerDialogインスタンスを取得*/
            val startDatePickerDialog = DatePickerDialog(
                    this@InputActivity,
                    { _, year, month, dayOfMonth ->
                        /*setした日付を取得して表示*/
                        startDate.setText(String.format("%02d / %02d", month + 1, dayOfMonth))
                        /*Calendarオブジェクトを作成*/
                        mStartAtDatetime.set(year, month, dayOfMonth)
                    },
                    year,
                    /*monthは0が1月のため-1する必要がある*/
                    month - 1,
                    day
            )

            /*dialogを表示*/
            startDatePickerDialog.show()
        }
        /*********************************************/

        /*終了日時EditTextが押されたとき*************/
        endDate.setOnClickListener {
            val intentIn = intent
            val year:Int = intentIn.getIntExtra("year", 0)
            val month:Int = intentIn.getIntExtra("month", 0)
            val day:Int = intentIn.getIntExtra("day", 0)
            /*DatePickerDialogインスタンスを取得*/
            val endDatePickerDialog = DatePickerDialog(
                    this@InputActivity,
                    { _, year, month, dayOfMonth ->
                        /*setした日付を取得して表示*/
                        endDate.setText(String.format("%02d / %02d", month + 1, dayOfMonth))
                        mEndAtDatetime.set(year, month, dayOfMonth)
                    },
                    year,
                    /*monthは0が1月のため-1する必要がある*/
                    month - 1,
                    day
            )

            /*dialogを表示*/
            endDatePickerDialog.show()
        }
        /*********************************************/

        /*時間入力ボタンが押されたとき***************/
        timeButton.setOnClickListener {
            /*詳細入力表示に*/
            timeButton.visibility = View.GONE
            timeArrow.visibility = View.VISIBLE
            startTime.visibility = View.VISIBLE
            endTime.visibility = View.VISIBLE
        }
        /*********************************************/

        /*開始時間EditTextが押されたとき*************/
        startTime.setOnClickListener {
            /*Calendarインスタンスを取得*/
            val startTimeCalendar = Calendar.getInstance()

            /*TimePickerDialogインスタンスを取得*/
            val startTimePickerDialog = TimePickerDialog(
                    this@InputActivity,
                    { _, hourOfDay, minute ->
                        /*setした時間を取得して表示*/
                        startTime.setText(String.format("%02d : %02d", hourOfDay, minute))
                        mStartAtDatetime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        mStartAtDatetime.set(Calendar.MINUTE, minute)
                    },
                    startTimeCalendar[Calendar.HOUR_OF_DAY],
                    startTimeCalendar[Calendar.MINUTE],
                    true
            )

            /*dialogを表示*/
            startTimePickerDialog.show()
        }
        /*********************************************/

        /*終了時間EditTextが押されたとき*************/
        endTime.setOnClickListener {
            /*Calendarインスタンスを取得*/
            val endTimeCalendar = Calendar.getInstance()

            /*TimePickerDialogインスタンスを取得*/
            val endTimePickerDialog = TimePickerDialog(
                    this@InputActivity,
                    { _, hourOfDay, minute ->
                        /*setした時間を取得して表示*/
                        endTime.setText(String.format("%02d : %02d", hourOfDay, minute))
                        /*Calendarオブジェクトを作成*/
                        mEndAtDatetime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        mEndAtDatetime.set(Calendar.MINUTE, minute)
                    },
                    endTimeCalendar[Calendar.HOUR_OF_DAY],
                    endTimeCalendar[Calendar.MINUTE],
                    true
            )

            /*dialogを表示*/
            endTimePickerDialog.show()
        }
        /*********************************************/

        /*表示色ボタンが押されたとき*****************/
        color1.setOnClickListener {
            /*色選択画面へ遷移*/
            goColorSelectActivity()
        }
        color2.setOnClickListener {
            /*色選択画面へ遷移*/
            goColorSelectActivity()
        }
        /*********************************************/

        /*詳細ボタンが押されたとき*******************/
        detailButton.setOnClickListener {
            /*詳細入力表示に*/
            detailButton.visibility = View.GONE
            mapCheckButton.visibility = View.VISIBLE
            place.visibility = View.VISIBLE
            memo.visibility = View.VISIBLE
        }
        /*********************************************/

        /*マップチェックボタンが押されたとき********/
        mapCheckButton.setOnClickListener {
            /*GoogleMapへ遷移*/
            goGoogleMap()
        }
        /*********************************************/

        /*完了ボタンが押されたとき*******************/
        doneButton.setOnClickListener {
            /*保存処理を実行*/
            mInputPresenter.saveSchedule(title.text.toString(), memo.text.toString(), mStartAtDatetime.time, mEndAtDatetime.time, mGroupId, 0)
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

    /*色画面遷移関数*********************************/
    private fun goColorSelectActivity() {
        /*色選択画面遷移用intent*/
        val intentOut = Intent(this, ColorSelectActivity::class.java)
        /*戻り値を設定して色選択画面に遷移*/
        startActivityForResult(intentOut, REQUEST_CODE)
    }
    /************************************************/

    /*GoogleMap画面遷移関数**************************/
    private fun goGoogleMap() {
        /*場所が入力されていない時(GoogleMapへ遷移)*/
        val gmmIntentUri: Uri
        gmmIntentUri = if (place.text.toString().isEmpty()) {
            /*geo:緯度,経度?q=検索文字列(緯度経度が"0,0"の場合、現在地表示)*/
            Uri.parse("geo:0,0")
        } else {
            val str = "geo:0,0?q=" + place.text.toString()
            Uri.parse(str)
        }
        /*GoogleMap用intent作成*/
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        /*GoogleMapがコール受信可能かをチェック*/
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        }
    }
    /************************************************/

    /*Activityから戻り値(色番号)を受け取る関数******/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE -> when (resultCode) {
                RESULT_OK -> {
                    /*色番号受け取り*/
                    colorNumber = data!!.getIntExtra("ColorNumber", 0) /*defaultValue:ColorNumberキーに値が入っていなかった時に返す値*/
                    /*DBからcolorNumberをキーにその要素を取得*/
                    repository.getScheduleGroup(
                            colorNumber,
                            object : GetScheduleGroupCallback {
                                override fun onScheduleGroupLoaded(group: ScheduleGroup) {
                                    /*色ボタン2に色名をセット*/
                                    color2.text = group.groupName
                                    /*色ボタン2に色をセット*/
                                    color2.setBackgroundColor(group.backgroundColor)
                                    /*色ボタン2にに文字色をセット*/
                                    if (group.characterColor == "黒") { /*黒ならば*/
                                        color2.setTextColor(Color.BLACK)
                                    } else { /*白ならば*/
                                        color2.setTextColor(Color.WHITE)
                                    }
                                    /*色ボタン2のテキストを左寄せに*/
                                    color2.gravity = Gravity.CENTER
                                    mGroupId = group.groupId
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

    /*inputActivityを終了させる関数*****************/
    override fun finishInput() {
        finish()
    }
    /************************************************/

    /*Presenterをsetする関数*************************/
    override fun setPresenter(presenter: Presenter?) {
        mInputPresenter = presenter!!
    }
    /************************************************/

    /*タッチイベントを取得し、フォーカスエリア外をタッチするとキーボードを閉じる*/
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && view is EditText && !view.javaClass.name.startsWith("android.webkit.")) {
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x = ev.rawX + view.getLeft() - scrcoords[0]
            val y = ev.rawY + view.getTop() - scrcoords[1]
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) (this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(this.window.decorView.applicationWindowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
    /************************************************/

    /*定数定義****************************************/
    companion object {
        private const val REQUEST_CODE = 1
    }
    /************************************************/
}