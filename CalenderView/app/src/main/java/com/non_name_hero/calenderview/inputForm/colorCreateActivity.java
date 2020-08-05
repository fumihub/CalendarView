package com.non_name_hero.calenderview.inputForm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.non_name_hero.calenderview.R;

public class colorCreateActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    private colorActivity colorActivity;

    private EditText colorCreateTitle;

    private Button color1;
    private Button color2;
    private Button cancelButton;
    private Button doneButton;

    private RadioButton textColorBlack;
    private RadioButton textColorWhite;

    //colorActivityでも使用したいためpublic
    public int colorNumberPre = 255;
    private int colorNumber = 255;
    private int color = 0;

    private Intent intentOut;

    //コンストラクタ
    public colorCreateActivity(){
        colorActivity = new colorActivity();
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
        textColorBlack = findViewById(R.id.RadioButton1);
        textColorBlack = findViewById(R.id.RadioButton2);

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
        //戻り値を設定して色画面に遷移
        startActivityForResult(intentOut, REQUEST_CODE);
    }

    public void returnColorSelectActivity(){
        //色選択遷移用intent
        intentOut = getIntent();
        //ボタンの色を遷移先へreturn
        intentOut.putExtra("Color", color);
        //色タイトルを遷移先へreturn
        intentOut.putExtra("ColorTitle", colorCreateTitle.getText());
        //文字色を遷移先へreturn

        setResult(RESULT_OK, intentOut);
        //押されたボタンに「×」印をつける
        colorActivity.checkFlag[colorNumber] = Boolean.TRUE;
        finish();
    }

    //Activityから戻り値(色番号)を受け取る処理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //colorActivityから戻ってきた場合
            case (REQUEST_CODE):
                if (resultCode == RESULT_OK) {
                    //色ボタンのテキスト色受け取り
                    colorNumber = data.getIntExtra("ColorNumber", 0);//defaultValue:ColorNumberキーに値が入っていなかった時に返す値
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
