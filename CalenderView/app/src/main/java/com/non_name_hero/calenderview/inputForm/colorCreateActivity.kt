package com.non_name_hero.calenderview.inputForm

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.data.ScheduleGroup
import com.non_name_hero.calenderview.data.source.ScheduleDataSource.GetScheduleGroupCallback
import com.non_name_hero.calenderview.data.source.ScheduleDataSource.SaveScheduleGroupCallback
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.utils.Injection
import java.lang.Boolean

class colorCreateActivity  //コンストラクタ
    : AppCompatActivity() {
    private var intentIn: Intent? = null
    private var intentOut: Intent? = null
    private var prefs: SharedPreferences? = null
    private var repository: ScheduleRepository? = null
    private var colorCreateTitle: EditText? = null
    private var color1: Button? = null
    private var color2: Button? = null
    private var cancelButton: Button? = null
    private var doneButton: Button? = null
    private var radioGroup: RadioGroup? = null
    private var blackRadioButton: RadioButton? = null
    private var whiteRadioButton: RadioButton? = null
    private var textColor: String? = null
    private var groupId = 0
    private var colorNumberPre = 43
    private var colorNumber = 43
    private var color = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = Injection.provideScheduleRepository(applicationContext)
        setContentView(R.layout.color_create)
        val myToolbar = findViewById<View>(R.id.colorCreateToolbar) as Toolbar
        setSupportActionBar(myToolbar)
        colorCreateTitle = findViewById(R.id.colorCreateTitle)
        color1 = findViewById(R.id.colorButton1)
        color2 = findViewById(R.id.colorButton2)
        cancelButton = findViewById(R.id.cancelButton)
        doneButton = findViewById(R.id.doneButton)
        radioGroup = findViewById(R.id.RadioGroup)
        blackRadioButton = findViewById(R.id.RadioButton1)
        whiteRadioButton = findViewById(R.id.RadioButton2)

        //TODO　色ボタン情報表示
        //SharedPreferenceからeditFlagの値を取得
        prefs = getSharedPreferences("input_data", MODE_PRIVATE)
        if (prefs.getBoolean("editFlag", Boolean.FALSE)) {
            //表示する色番号を取得
            intentIn = intent
            colorNumberPre = intentIn.getIntExtra("ColorNumberPre", 43)
            colorNumber = colorNumberPre
            //DBからcolorNumberをキーにその要素を取得
            repository!!.getScheduleGroup(
                    colorNumberPre,
                    object : GetScheduleGroupCallback {
                        override fun onScheduleGroupLoaded(group: ScheduleGroup?) {
                            //グループIDの取得
                            groupId = group!!.groupId
                            //色タイトルに色名をセット
                            colorCreateTitle.setText(group.groupName)
                            //色ボタン2に色をセット
                            color = group.backgroundColor
                            color2.setBackgroundColor(color)
                            //色ボタン2にに文字色をセット
                            if (group.characterColor == "黒") { //黒ならば
                                blackRadioButton.setChecked(true)
                            } else { //白ならば
                                whiteRadioButton.setChecked(true)
                            }
                        }
                    }
            )
        }

        /*色ボタンが押されたとき**************************/
        color1.setOnClickListener(View.OnClickListener { //色画面に遷移
            goColorActivity()
        })
        color2.setOnClickListener(View.OnClickListener { //色画面に遷移
            goColorActivity()
        })
        /** */

        /*キャンセルボタンが押されたとき******************/
        cancelButton.setOnClickListener(View.OnClickListener { //色選択画面に遷移
            finish()
        })
        /** */

        /*保存ボタンが押されたとき************************/
        doneButton.setOnClickListener(View.OnClickListener { //色選択画面に遷移
            returnColorSelectActivity()
        })
        /** */
    }

    fun goColorActivity() {
        //色画面遷移用intent
        intentOut = Intent(this, colorActivity::class.java)
        //色番号前回値を引数で色画面に渡す
        intentOut!!.putExtra("colorNumberPre", colorNumberPre)
        //戻り値を設定して色画面に遷移
        startActivityForResult(intentOut, REQUEST_CODE)
    }

    fun returnColorSelectActivity() {
        val checkedId = radioGroup!!.checkedRadioButtonId
        if (checkedId != -1) {
            // 選択されているラジオボタンの取得
            // (Fragmentの場合は「getActivity().findViewById」にする)
            val radioButton = findViewById<View>(checkedId) as RadioButton

            // ラジオボタンのテキスト(色)を取得
            textColor = radioButton.text.toString()
        }

        //エラー処理
        if (color == 0 || textColor == null || colorCreateTitle!!.text.toString().isEmpty()) {
            //トースト表示
            val errorToast = Toast.makeText(
                    applicationContext,
                    "全ての項目を埋めてください！",
                    Toast.LENGTH_SHORT
            )
            errorToast.show()
        } else {
            //編集画面の場合
            if (prefs!!.getBoolean("editFlag", Boolean.FALSE)) {
                repository!!.updateScheduleGroup(
                        ScheduleGroup(
                                groupId,
                                colorNumber,
                                colorCreateTitle!!.text.toString(),
                                textColor!!,
                                color
                        ),
                        object : SaveScheduleGroupCallback {
                            override fun onScheduleGroupSaved() {
                                setResult(RESULT_OK, intentOut)
                                finish()
                            }

                            override fun onDataNotSaved() {}
                        })
            } else {
                repository!!.insertScheduleGroup(
                        ScheduleGroup(
                                colorNumber,
                                colorCreateTitle!!.text.toString(),
                                textColor!!,
                                color
                        ),
                        object : SaveScheduleGroupCallback {
                            override fun onScheduleGroupSaved() {
                                setResult(RESULT_OK, intentOut)
                                finish()
                            }

                            override fun onDataNotSaved() {}
                        }
                )
            }
        }
    }

    //Activityから戻り値(色番号、文字色、背景色)を受け取る処理
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE -> if (resultCode == RESULT_OK) {
                //色番号
                colorNumber = data!!.getIntExtra("ColorNumber", 0) //defaultValue:ColorNumberキーに値が入っていなかった時に返す値
                //色ボタンのテキスト色受け取り
                color = data.getIntExtra("Color", 0)

                //ボタンの背景色を色ボタンの色に変更
                color2!!.setBackgroundColor(color)

                //colorNumberを前回値として保持
                colorNumberPre = colorNumber
            } else if (resultCode == RESULT_CANCELED) {
                //キャンセルボタンを押して戻ってきたときの処理
            } else {
                //その他
            }
            else -> {
            }
        }
    }

    companion object {
        private const val REQUEST_CODE = 1
    }
}