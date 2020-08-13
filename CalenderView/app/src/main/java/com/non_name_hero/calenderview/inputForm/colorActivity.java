package com.non_name_hero.calenderview.inputForm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.non_name_hero.calenderview.R;

public class colorActivity  extends AppCompatActivity {

    private final int ARRAYLENGTH = 49;

    private Intent intentIn;
    private Intent intentOut;

    private Button[] colorButton = new Button[49];
    private TextView[] checkText = new TextView[49];
    private int[] colorButtonId = {R.id.redButton1, R.id.redButton2, R.id.redButton3, R.id.redButton4, R.id.redButton5, R.id.redButton6, R.id.redButton7,
                                    R.id.purpleButton1, R.id.purpleButton2, R.id.purpleButton3, R.id.purpleButton4, R.id.purpleButton5, R.id.purpleButton6, R.id.purpleButton7,
                                    R.id.blueButton1, R.id.blueButton2, R.id.blueButton3, R.id.blueButton4, R.id.blueButton5, R.id.blueButton6, R.id.blueButton7,
                                    R.id.greenButton1, R.id.greenButton2, R.id.greenButton3, R.id.greenButton4, R.id.greenButton5, R.id.greenButton6, R.id.greenButton7,
                                    R.id.yellowButton1, R.id.yellowButton2, R.id.yellowButton3, R.id.yellowButton4, R.id.yellowButton5, R.id.yellowButton6, R.id.yellowButton7,
                                    R.id.brownButton1, R.id.brownButton2, R.id.brownButton3, R.id.brownButton4, R.id.brownButton5, R.id.brownButton6, R.id.brownButton7,
                                    R.id.blackButton1, R.id.blackButton2, R.id.blackButton3, R.id.blackButton4, R.id.blackButton5, R.id.blackButton6, R.id.blackButton7};
    private int[] checkTextId = {R.id.redCheckText1, R.id.redCheckText2, R.id.redCheckText3, R.id.redCheckText4, R.id.redCheckText5, R.id.redCheckText6, R.id.redCheckText7,
                                   R.id.purpleCheckText1, R.id.purpleCheckText2, R.id.purpleCheckText3, R.id.purpleCheckText4, R.id.purpleCheckText5, R.id.purpleCheckText6, R.id.purpleCheckText7,
                                    R.id.blueCheckText1, R.id.blueCheckText2, R.id.blueCheckText3, R.id.blueCheckText4, R.id.blueCheckText5, R.id.blueCheckText6, R.id.blueCheckText7,
                                    R.id.greenCheckText1, R.id.greenCheckText2, R.id.greenCheckText3, R.id.greenCheckText4, R.id.greenCheckText5, R.id.greenCheckText6, R.id.greenCheckText7,
                                    R.id.yellowCheckText1, R.id.yellowCheckText2, R.id.yellowCheckText3, R.id.yellowCheckText4, R.id.yellowCheckText5, R.id.yellowCheckText6, R.id.yellowCheckText7,
                                    R.id.brownCheckText1, R.id.brownCheckText2, R.id.brownCheckText3, R.id.brownCheckText4, R.id.brownCheckText5, R.id.brownCheckText6, R.id.brownCheckText7,
                                    R.id.blackCheckText1, R.id.blackCheckText2, R.id.blackCheckText3, R.id.blackCheckText4, R.id.blackCheckText5, R.id.blackCheckText6, R.id.blackCheckText7};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.color);
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.colorToolbar);
        setSupportActionBar(myToolbar);

        intentIn = getIntent();
        //チェックフラグ取得
        boolean[] checkFlag = intentIn.getBooleanArrayExtra("checkFlag");

        /* 変数宣言 */
        for (int cnt = 0; cnt < ARRAYLENGTH; cnt++) {
            colorButton[cnt] = findViewById(colorButtonId[cnt]);
            checkText[cnt] = findViewById(checkTextId[cnt]);
        }

        /* 最初表示判定 */
        for (int cnt = 0; cnt < ARRAYLENGTH; cnt++) {
            if (checkFlag[cnt]) {
                checkText[cnt].setVisibility(View.VISIBLE);
            }
            else {
                checkText[cnt].setVisibility(View.INVISIBLE);
            }
            colorButton[cnt].setTag(cnt);
            colorButton[cnt].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnColorCreateActivity((int)(v.getTag()));
                }
            });
        }

        //色番号前回値を取得
        int colorNumberPre = intentIn.getIntExtra("colorNumberPre", 255);
        //前回押された色ボタンのテキストを「〇」に
        //初回
        if (colorNumberPre == 255) {//255：colorNumberPreの初期値
            /* 何もしない */
        }
        //初回以外
        else {
            colorButton[colorNumberPre].setText("○");
        }

    }

    public void returnColorCreateActivity(int colorNumber) {

        intentOut = getIntent();
        //ボタンの色番号を遷移先へreturn
        intentOut.putExtra("ColorNumber", colorNumber);
        //ボタンのテキストの色を遷移先へreturn
        intentOut.putExtra("Color", colorButton[colorNumber].getCurrentTextColor());
        setResult(RESULT_OK, intentOut);

        //色作成画面に遷移
        finish();
    }
}

