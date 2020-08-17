package com.non_name_hero.calenderview.inputForm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.non_name_hero.calenderview.R;

public class colorCreateActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    private Intent intentOut;

    private SharedPreferences prefs;

    private EditText colorCreateTitle;

    private Button color1;
    private Button color2;
    private Button cancelButton;
    private Button doneButton;

    private RadioGroup radioGroup;

    private String textColor;

    private int colorNumberPre = 255;
    private int colorNumber = 255;
    private int color = 0;

    //コンストラクタ
    public colorCreateActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.color_create);
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.colorCreateToolbar);
        setSupportActionBar(myToolbar);

        colorCreateTitle = findViewById(R.id.colorCreateTitle);
        color1 = findViewById(R.id.colorButton1);
        color2 = findViewById(R.id.colorButton2);
        cancelButton = findViewById(R.id.cancelButton);
        doneButton = findViewById(R.id.doneButton);
        radioGroup = findViewById(R.id.RadioGroup);

        /*色ボタンが押されたとき**************************/
        color1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //色画面に遷移
                goColorActivity();
            }
        });
        color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //色画面に遷移
                goColorActivity();
            }
        });
        /************************************************/

        /*キャンセルボタンが押されたとき******************/
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //色選択画面に遷移
                finish();
            }
        });
        /************************************************/

        /*保存ボタンが押されたとき************************/
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //色選択画面に遷移
                returnColorSelectActivity();
            }
        });
        /************************************************/

    }

    public void goColorActivity(){
        //色画面遷移用intent
        intentOut = new Intent(this, colorActivity.class);
        //色番号前回値を引数で色画面に渡す
        intentOut.putExtra("colorNumberPre", colorNumberPre);
        //戻り値を設定して色画面に遷移
        startActivityForResult(intentOut, REQUEST_CODE);
    }

    public void returnColorSelectActivity(){

        int checkedId = radioGroup.getCheckedRadioButtonId();
        if (checkedId != -1) {
            // 選択されているラジオボタンの取得
            // (Fragmentの場合は「getActivity().findViewById」にする)
            RadioButton radioButton = (RadioButton) findViewById(checkedId);

            // ラジオボタンのテキスト(色)を取得
            textColor = radioButton.getText().toString();
        }

        //エラー処理
        if (color == 0 || textColor == "" || colorCreateTitle.getText().toString() == "") {
            //トースト表示
            Toast errorToast = Toast.makeText(
                    getApplicationContext(),
                    "全ての項目を埋めてください！",
                    Toast.LENGTH_SHORT
            );
            errorToast.show();
        }
        else {
            //色選択遷移用intent
            intentOut = getIntent();
            //ボタンの色を遷移先へreturn
            intentOut.putExtra("Color", color);
            //色タイトルを遷移先へreturn
            intentOut.putExtra("ColorTitle", colorCreateTitle.getText().toString());
            //文字色を遷移先へreturn
            intentOut.putExtra("textColor", textColor);
            //ボタンの色番号を遷移先へreturn
            intentOut.putExtra("ColorNumber", colorNumber);
            setResult(RESULT_OK, intentOut);
            //colorNumberをString型に変換
            String strColorNumber = String.valueOf(colorNumber);
            //SharedPreferenceに「colorNumber(数字)」の形で保存
            //↓ここでエラーが起きる
            prefs = getSharedPreferences("SaveData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(strColorNumber,Boolean.TRUE);
            finish();
        }

    }

    //Activityから戻り値(色番号)を受け取る処理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //colorActivityから戻ってきた場合
            case (REQUEST_CODE):
                if (resultCode == RESULT_OK) {
                    //色番号
                    colorNumber = data.getIntExtra("ColorNumber", 0);//defaultValue:ColorNumberキーに値が入っていなかった時に返す値
                    //色ボタンのテキスト色受け取り
                    color = data.getIntExtra("Color",0);

                    //ボタンの背景色を色ボタンの色に変更
                    color2.setBackgroundColor(color);

                    //colorNumberを前回値として保持
                    colorNumberPre = colorNumber;
                }
                else if (resultCode == RESULT_CANCELED) {
                    //キャンセルボタンを押して戻ってきたときの処理
                }
                else {
                    //その他
                }
                break;
            default:
                break;
        }
    }
}
