package com.non_name_hero.calenderview.inputForm

import android.content.Intent
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
import java.lang.Boolean.FALSE

class colorCreateActivity  /*コンストラクタ*/
    : AppCompatActivity() {

    private var repository: ScheduleRepository? = null

    private lateinit var colorCreateTitle: EditText
    private lateinit var color1: Button
    private lateinit var color2: Button
    private lateinit var cancelButton: Button
    private lateinit var doneButton: Button
    private lateinit var radioGroup: RadioGroup
    private lateinit var blackRadioButton: RadioButton
    private lateinit var whiteRadioButton: RadioButton

    private val intentOut = Intent(this, colorSelectActivity::class.java)

    private var textColor: String = ""

    private var groupId = 0
    private var colorNumberPre = 43
    private var colorNumber = 43
    private var color = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*初期設定***************************************/
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
        /************************************************/

        /*色ボタン情報表示*******************************/
        /*SharedPreferenceからeditFlagの値を取得*/
        val prefs = getSharedPreferences("input_data", MODE_PRIVATE)
        if (prefs.getBoolean("editFlag", FALSE)) {
            /*表示する色番号を取得*/
            val intentIn = intent
            colorNumberPre = intentIn.getIntExtra("ColorNumberPre", 43)
            colorNumber = colorNumberPre
            /*DBからcolorNumberをキーにその要素を取得*/
            repository!!.getScheduleGroup(
                    colorNumberPre,
                    object : GetScheduleGroupCallback {
                        override fun onScheduleGroupLoaded(group: ScheduleGroup?) {
                            /*グループIDの取得*/
                            groupId = group!!.groupId
                            /*色タイトルに色名をセット*/
                            colorCreateTitle.setText(group.groupName)
                            /*色ボタン2に色をセット*/
                            color = group.backgroundColor
                            color2.setBackgroundColor(color)
                            /*色ボタン2にに文字色をセット*/
                            if (group.characterColor == "黒") { /*黒ならば*/
                                blackRadioButton.isChecked = true
                            } else { /*白ならば*/
                                whiteRadioButton.isChecked = true
                            }
                        }
                    }
            )
        }
        /************************************************/

        /*色ボタンが押されたとき************************/
        color1.setOnClickListener {
            /*色画面に遷移*/
            goColorActivity()
        }
        color2.setOnClickListener {
            /*色画面に遷移*/
            goColorActivity()
        }
        /************************************************/

        /*キャンセルボタンが押されたとき****************/
        cancelButton.setOnClickListener {
            /*色選択画面に遷移*/
            finish()
        }
        /************************************************/

        /*保存ボタンが押されたとき**********************/
        doneButton.setOnClickListener {
            /*色選択画面に遷移*/
            returnColorSelectActivity()
        }
        /************************************************/
    }

    /*色画面遷移関数************************************/
    private fun goColorActivity() {
        /*色画面遷移用intent*/
        val intentColorActivity = Intent(this, colorActivity::class.java)
        /*色番号前回値を引数で色画面に渡す*/
        intentColorActivity.putExtra("colorNumberPre", colorNumberPre)
        /*戻り値を設定して色画面に遷移*/
        startActivityForResult(intentColorActivity, REQUEST_CODE)
    }
    /************************************************/

    /*色選択画面遷移関数****************************/
    private fun returnColorSelectActivity() {
        /*ラジオボタン処理**************************/
        val checkedId = radioGroup.checkedRadioButtonId
        if (checkedId != -1) {
            /*選択されているラジオボタンの取得*/
            /*(Fragmentの場合は「getActivity().findViewById」にする)*/
            val radioButton = findViewById<View>(checkedId) as RadioButton

            /*ラジオボタンのテキスト(色)を取得*/
            this.textColor = radioButton.text.toString()
        }
        /*******************************************/

        /*エラー処理*******************************/
        if (color == 0 || this.textColor == "" || colorCreateTitle.text.toString().isEmpty()) {
            /*トースト表示*/
            val errorToast = Toast.makeText(
                    applicationContext,
                    "全ての項目を埋めてください！",
                    Toast.LENGTH_SHORT
            )
            errorToast.show()
        /*******************************************/
        } else {
            /*編集画面でない場合***********************/
            /*SharedPreferenceからeditFlagの値を取得*/
            val prefs = getSharedPreferences("input_data", MODE_PRIVATE)
            if (!prefs.getBoolean("editFlag", FALSE)) {
                repository!!.insertScheduleGroup(
                        ScheduleGroup(
                                colorNumber,
                                colorCreateTitle.text.toString(),
                                textColor,
                                color
                        ),
                        object : SaveScheduleGroupCallback {
                            override fun onScheduleGroupSaved() {
                                /*色選択画面遷移*/
                                setResult(RESULT_OK, intentOut)
                                finish()
                            }

                            override fun onDataNotSaved() {}
                        }
                )
            /*******************************************/
            /*編集画面の場合***************************/
            } else {
                repository!!.updateScheduleGroup(
                        ScheduleGroup(
                                groupId,
                                colorNumber,
                                colorCreateTitle.text.toString(),
                                textColor,
                                color
                        ),
                        object : SaveScheduleGroupCallback {
                            override fun onScheduleGroupSaved() {
                                /*色選択画面遷移*/
                                setResult(RESULT_OK, intentOut)
                                finish()
                            }

                            override fun onDataNotSaved() {}
                        })

            }
            /*******************************************/
        }
    }
    /************************************************/

    /*Activityから戻り値(色番号、文字色、背景色)を受け取る処理*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE -> when (resultCode) {
                RESULT_OK -> {
                    /*色番号*/
                    /*defaultValue:ColorNumberキーに値が入っていなかった時に返す値*/
                    colorNumber = data!!.getIntExtra("ColorNumber", 0)
                    /*色ボタンのテキスト色受け取り*/
                    color = data.getIntExtra("Color", 0)
                    /*ボタンの背景色を色ボタンの色に変更*/
                    color2.setBackgroundColor(color)
                    /*colorNumberを前回値として保持*/
                    colorNumberPre = colorNumber
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