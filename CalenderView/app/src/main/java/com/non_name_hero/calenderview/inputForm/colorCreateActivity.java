package com.non_name_hero.calenderview.inputForm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.non_name_hero.calenderview.R;

public class colorCreateActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    private EditText colorCreateTitle;

    private Button color1;
    private Button color2;
    private Button cancelButton;
    private Button doneButton;

    private int[] colorId = {R.color.redColor1, R.color.redColor2, R.color.redColor3, R.color.redColor4, R.color.redColor5, R.color.redColor6, R.color.redColor7,
            R.color.purpleColor1, R.color.purpleColor2, R.color.purpleColor3, R.color.purpleColor4, R.color.purpleColor5, R.color.purpleColor6, R.color.purpleColor7,
            R.color.blueColor1, R.color.blueColor2, R.color.blueColor3, R.color.blueColor4, R.color.blueColor5, R.color.blueColor6, R.color.blueColor7,
            R.color.greenColor1, R.color.greenColor2, R.color.greenColor3, R.color.greenColor4, R.color.greenColor5, R.color.greenColor6, R.color.greenColor7,
            R.color.yellowColor1, R.color.yellowColor2, R.color.yellowColor3, R.color.yellowColor4, R.color.yellowColor5, R.color.yellowColor6, R.color.yellowColor7,
            R.color.brownColor1, R.color.brownColor2, R.color.brownColor3, R.color.brownColor4, R.color.brownColor5, R.color.brownColor6, R.color.brownColor7,
            R.color.blackColor1, R.color.blackColor2, R.color.blackColor3, R.color.blackColor4, R.color.blackColor5, R.color.blackColor6, R.color.blackColor7};


    private Intent intentOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.color_create);
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.colorCreateToolbar);
        setSupportActionBar(myToolbar);

        //色画面遷移用intent
        intentOut = new Intent(this, colorActivity.class);

        colorCreateTitle = findViewById(R.id.colorCreateTitle);
        color1 = findViewById(R.id.colorButton1);
        color2 = findViewById(R.id.colorButton2);
        cancelButton = findViewById(R.id.cancelButton);
        doneButton = findViewById(R.id.doneButton);

        /*色ボタンが押されたとき**************************/
        color1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //戻り値を設定して色画面に遷移
                startActivityForResult(intentOut, REQUEST_CODE);
            }
        });
        color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //戻り値を設定して色画面に遷移
                startActivityForResult(intentOut, REQUEST_CODE);
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
                finish();
            }
        });
        /************************************************/

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
                    int colorNumber = data.getIntExtra("ColorNumber", 0);

                    //ボタンの背景色を色ボタンの色に変更
                    color2.setBackgroundColor(colorNumber);
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
