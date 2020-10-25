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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.data.ScheduleGroup
import com.non_name_hero.calenderview.data.source.ScheduleDataSource.GetScheduleGroupCallback
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.inputForm.InputActivity
import com.non_name_hero.calenderview.inputForm.InputContract.Presenter
import com.non_name_hero.calenderview.inputForm.colorSelectActivity
import com.non_name_hero.calenderview.utils.Injection
import java.util.*

class InputActivity  //コンストラクタ
    : AppCompatActivity(), InputContract.View {
    private var mInputPresenter: Presenter? = null
    private var repository: ScheduleRepository? = null
    private var mStartAtDatetime: Calendar? = null
    private var mEndAtDatetime: Calendar? = null
    private var title: EditText? = null
    private var startDate: EditText? = null
    private var endDate: EditText? = null
    private var startTime: EditText? = null
    private var endTime: EditText? = null
    private var timeArrow: TextView? = null
    private var place: TextView? = null
    private var memo: EditText? = null
    private var timeButton: Button? = null
    private var color1: Button? = null
    private var color2: Button? = null
    private var mapCheckButton1: Button? = null
    private var mapCheckButton2: Button? = null
    private var mapCheckButton3: Button? = null
    private var mapCheckButton4: Button? = null
    private var detailButton: Button? = null
    private var cancelButton: Button? = null
    private var doneButton: Button? = null
    private var intentIn: Intent? = null
    private var intentOut: Intent? = null
    private var year: String? = null
    private var month: String? = null
    private var day: String? = null
    private var colorNumber = 0
    private var mGroupId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = Injection.provideScheduleRepository(applicationContext)
        setContentView(R.layout.input_main)
        val myToolbar = findViewById<View>(R.id.inputToolbar) as Toolbar
        setSupportActionBar(myToolbar)
        InputPresenter(this, Injection.provideScheduleRepository(applicationContext)!!)

        //カレンダー初期値用
        intentIn = intent
        year = intentIn.getStringExtra("year")
        month = intentIn.getStringExtra("month")
        day = intentIn.getStringExtra("day")
        //グループIDの初期値設定
        mGroupId = 1

        //色選択画面遷移用intent
        intentOut = Intent(this, colorSelectActivity::class.java)


        /*入力画面表示*********************************************************************/
        //カレンダーセルのボタンが押された場合
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
        mapCheckButton1 = findViewById(R.id.mapCheckButton1)
        mapCheckButton2 = findViewById(R.id.mapCheckButton2)
        mapCheckButton3 = findViewById(R.id.mapCheckButton3)
        mapCheckButton4 = findViewById(R.id.mapCheckButton4)
        detailButton = findViewById(R.id.detailButton)
        detailButton = findViewById(R.id.detailButton)
        place = findViewById(R.id.place)
        memo = findViewById(R.id.memo)
        cancelButton = findViewById(R.id.cancelButton)
        doneButton = findViewById(R.id.doneButton)

        //最初表示
        title.setVisibility(View.VISIBLE)
        startDate.setVisibility(View.VISIBLE)
        endDate.setVisibility(View.VISIBLE)
        timeButton.setVisibility(View.VISIBLE)
        color1.setVisibility(View.VISIBLE)
        color2.setVisibility(View.VISIBLE)
        detailButton.setVisibility(View.VISIBLE)
        cancelButton.setVisibility(View.VISIBLE)
        doneButton.setVisibility(View.VISIBLE)
        //最初非表示
        startTime.setVisibility(View.GONE)
        endTime.setVisibility(View.GONE)
        timeArrow.setVisibility(View.GONE)
        mapCheckButton1.setVisibility(View.GONE)
        mapCheckButton2.setVisibility(View.GONE)
        mapCheckButton3.setVisibility(View.GONE)
        mapCheckButton4.setVisibility(View.GONE)
        place.setVisibility(View.GONE)
        memo.setVisibility(View.GONE)
        //初期表示日付取得
        mStartAtDatetime = Calendar.getInstance()
        mStartAtDatetime.set(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day))
        mEndAtDatetime = Calendar.getInstance()
        mEndAtDatetime.set(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day))
        //        /*タイトルEditTextが押されたとき********************/
//        title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //フォーカスを当てる
//                title.setFocusable(true);
//                title.setFocusableInTouchMode(true);
//            }
//        });
//        /**************************************************/

        //初期値を設定
        startDate.setText("$month/$day")
        endDate.setText("$month/$day")

        /*開始日時EditTextが押されたとき********************/startDate.setOnClickListener(View.OnClickListener { //Calendarインスタンスを取得
            val startCalendar = Calendar.getInstance()
            val intentIn = intent
            val year = Integer.valueOf(intentIn.getStringExtra("year"))
            val month = Integer.valueOf(intentIn.getStringExtra("month"))
            val day = Integer.valueOf(intentIn.getStringExtra("day"))
            //DatePickerDialogインスタンスを取得
            val startDatePickerDialog = DatePickerDialog(
                    this@InputActivity,
                    { view, year, month, dayOfMonth -> //setした日付を取得して表示
                        startDate.setText(String.format("%02d / %02d", month + 1, dayOfMonth))
                        //Calendarオブジェクトを作成
                        mStartAtDatetime.set(year, month, dayOfMonth)
                    },
                    year,  //monthは0が1月のため-1する必要がある
                    month - 1,
                    day
            )

            //dialogを表示
            startDatePickerDialog.show()
        })
        /** */

        /*終了日時EditTextが押されたとき********************/endDate.setOnClickListener(View.OnClickListener { //Calendarインスタンスを取得
            val endCalendar = Calendar.getInstance()
            val intentIn = intent
            val year = Integer.valueOf(intentIn.getStringExtra("year"))
            val month = Integer.valueOf(intentIn.getStringExtra("month"))
            val day = Integer.valueOf(intentIn.getStringExtra("day"))
            //DatePickerDialogインスタンスを取得
            val endDatePickerDialog = DatePickerDialog(
                    this@InputActivity,
                    { view, year, month, dayOfMonth -> //setした日付を取得して表示
                        endDate.setText(String.format("%02d / %02d", month + 1, dayOfMonth))
                        mEndAtDatetime.set(year, month, dayOfMonth)
                    },
                    year,  //monthは0が1月のため-1する必要がある
                    month - 1,
                    day
            )

            //dialogを表示
            endDatePickerDialog.show()
        })
        /** */

        /*時間入力ボタンが押されたとき********************/timeButton.setOnClickListener(View.OnClickListener { //詳細入力表示に
            timeButton.setVisibility(View.GONE)
            timeArrow.setVisibility(View.VISIBLE)
            startTime.setVisibility(View.VISIBLE)
            endTime.setVisibility(View.VISIBLE)
        })
        /** */

        /*開始時間EditTextが押されたとき********************/startTime.setOnClickListener(View.OnClickListener { //Calendarインスタンスを取得
            val startTimeCalendar = Calendar.getInstance()

            //TimePickerDialogインスタンスを取得
            val startTimePickerDialog = TimePickerDialog(
                    this@InputActivity,
                    { view, hourOfDay, minute -> //setした時間を取得して表示
                        startTime.setText(String.format("%02d : %02d", hourOfDay, minute))
                        mStartAtDatetime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        mStartAtDatetime.set(Calendar.MINUTE, minute)
                    },
                    startTimeCalendar[Calendar.HOUR_OF_DAY],
                    startTimeCalendar[Calendar.MINUTE],
                    true
            )

            //dialogを表示
            startTimePickerDialog.show()
        })
        /** */

        /*終了時間EditTextが押されたとき********************/endTime.setOnClickListener(View.OnClickListener { //Calendarインスタンスを取得
            val endTimeCalendar = Calendar.getInstance()

            //TimePickerDialogインスタンスを取得
            val endTimePickerDialog = TimePickerDialog(
                    this@InputActivity,
                    { view, hourOfDay, minute -> //setした時間を取得して表示
                        endTime.setText(String.format("%02d : %02d", hourOfDay, minute))
                        //Calendarオブジェクトを作成
                        mEndAtDatetime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        mEndAtDatetime.set(Calendar.MINUTE, minute)
                    },
                    endTimeCalendar[Calendar.HOUR_OF_DAY],
                    endTimeCalendar[Calendar.MINUTE],
                    true
            )

            //dialogを表示
            endTimePickerDialog.show()
        })
        /** */

        /*表示色ボタンが押されたとき*********************/color1.setOnClickListener(View.OnClickListener { //色選択画面へ遷移
            goColorSelectActivity()
        })
        color2.setOnClickListener(View.OnClickListener { //色選択画面へ遷移
            goColorSelectActivity()
        })
        /** */

        /*詳細ボタンが押されたとき************************/detailButton.setOnClickListener(View.OnClickListener { //詳細入力表示に
            detailButton.setVisibility(View.GONE)
            mapCheckButton1.setVisibility(View.VISIBLE)
            mapCheckButton2.setVisibility(View.VISIBLE)
            mapCheckButton3.setVisibility(View.VISIBLE)
            mapCheckButton4.setVisibility(View.VISIBLE)
            place.setVisibility(View.VISIBLE)
            memo.setVisibility(View.VISIBLE)
        })
        /** */

        /*マップチェックボタンが押されたとき*********************/mapCheckButton1.setOnClickListener(View.OnClickListener { //GoogleMapへ遷移
            goGoogleMap()
        })
        mapCheckButton2.setOnClickListener(View.OnClickListener { //GoogleMapへ遷移
            goGoogleMap()
        })
        mapCheckButton3.setOnClickListener(View.OnClickListener { //GoogleMapへ遷移
            goGoogleMap()
        })
        mapCheckButton4.setOnClickListener(View.OnClickListener { //GoogleMapへ遷移
            goGoogleMap()
        })
        /** */

        /*完了ボタンが押されたとき************************/doneButton.setOnClickListener(View.OnClickListener {
            //保存処理を実行
            mInputPresenter!!.saveSchedule(title.getText().toString(), memo.getText().toString(), mStartAtDatetime.getTime(), mEndAtDatetime.getTime(), mGroupId, 0)
            //カレンダー表示画面に遷移
        })
        /** */

        /*キャンセルボタンが押されたとき******************/cancelButton.setOnClickListener(View.OnClickListener { //カレンダー表示画面に遷移
            finish()
        })
        /** */
        /** */
    }

    fun goColorSelectActivity() {
        //戻り値を設定して色選択画面に遷移
        startActivityForResult(intentOut, REQUEST_CODE)
    }

    fun goGoogleMap() {
        //場所が入力されていない時(GoogleMapへ遷移)
        val gmmIntentUri: Uri
        gmmIntentUri = if (place!!.text.toString().isEmpty()) {
            //geo:緯度,経度?q=検索文字列(緯度経度が"0,0"の場合、現在地表示)
            Uri.parse("geo:0,0")
        } else {
            val str = "geo:0,0?q=" + place!!.text.toString()
            Uri.parse(str)
        }
        //GoogleMap用intent作成
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        //GoogleMapがコール受信可能かをチェック
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        }
    }

    //Activityから戻り値(色番号)を受け取る処理
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE -> if (resultCode == RESULT_OK) {
                //色番号受け取り
                colorNumber = data!!.getIntExtra("ColorNumber", 0) //defaultValue:ColorNumberキーに値が入っていなかった時に返す値
                //DBからcolorNumberをキーにその要素を取得
                repository!!.getScheduleGroup(
                        colorNumber,
                        object : GetScheduleGroupCallback {
                            override fun onScheduleGroupLoaded(group: ScheduleGroup?) {
                                //色ボタン2に色名をセット
                                color2!!.text = group!!.groupName
                                //色ボタン2に色をセット
                                color2!!.setBackgroundColor(group.backgroundColor)
                                //色ボタン2にに文字色をセット
                                if (group.characterColor == "黒") { //黒ならば
                                    color2!!.setTextColor(Color.BLACK)
                                } else { //白ならば
                                    color2!!.setTextColor(Color.WHITE)
                                }
                                //色ボタン2のテキストを左寄せに
                                color2!!.gravity = Gravity.CENTER
                                mGroupId = group.groupId
                            }
                        }
                )
            } else if (resultCode == RESULT_CANCELED) {
                //キャンセルボタンを押して戻ってきたときの処理
            } else {
                //その他
            }
            else -> {
            }
        }
    }

    override fun finishInput() {
        finish()
    }

    override fun setPresenter(presenter: Presenter?) {
        mInputPresenter = presenter
    }

    /**
     * タッチイベントを取得し、フォーカスエリア外をタッチするとキーボードを閉じる
     */
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

    companion object {
        private const val REQUEST_CODE = 1
    }
}