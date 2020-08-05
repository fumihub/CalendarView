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

    private colorCreateActivity colorCreateActivity;
    private Intent intentOut;

    //色内容が保存されたときに変更するためpublic
    public Boolean[] checkFlag = new Boolean[49];

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


    //コンストラクタ
    public colorActivity(){
        colorCreateActivity = new colorCreateActivity();
        /* フラグ初期化 */
        for (int cnt = 0; cnt < ARRAYLENGTH; cnt++) {
            checkFlag[cnt] = Boolean.FALSE;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.color);
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.colorToolbar);
        setSupportActionBar(myToolbar);

        checkFlag[42] = Boolean.TRUE;

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
        }

        //前回押された色ボタンのテキストを「〇」に
        //初回
        if (colorCreateActivity.colorNumberPre == 255) {//255：colorNumberPreの初期値
            /* 何もしない */
        }
        //初回以外
        else {
            colorButton[colorCreateActivity.colorNumberPre].setText("○");
        }

        /*色ボタンが押されたとき***************************/
        colorButton[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(0);
            }
        });

        colorButton[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(1);
            }
        });

        colorButton[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(2);
            }
        });

        colorButton[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(3);
            }
        });

        colorButton[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(4);
            }
        });

        colorButton[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(5);
            }
        });

        colorButton[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(6);
            }
        });

        colorButton[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(7);
            }
        });

        colorButton[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(8);
            }
        });

        colorButton[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(9);
            }
        });

        colorButton[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(10);
            }
        });

        colorButton[11].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(11);
            }
        });

        colorButton[12].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(12);
            }
        });

        colorButton[13].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(13);
            }
        });

        colorButton[14].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(14);
            }
        });

        colorButton[15].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(15);
            }
        });

        colorButton[16].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(16);
            }
        });

        colorButton[17].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(17);
            }
        });

        colorButton[18].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(18);
            }
        });

        colorButton[19].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(19);
            }
        });

        colorButton[20].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(20);
            }
        });

        colorButton[21].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(21);
            }
        });

        colorButton[22].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(22);
            }
        });

        colorButton[23].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(23);
            }
        });

        colorButton[24].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(24);
            }
        });

        colorButton[25].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(25);
            }
        });

        colorButton[26].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(26);
            }
        });

        colorButton[27].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(27);
            }
        });

        colorButton[28].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(28);
            }
        });

        colorButton[29].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(29);
            }
        });

        colorButton[30].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(30);
            }
        });

        colorButton[31].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(31);
            }
        });

        colorButton[32].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(32);
            }
        });

        colorButton[33].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(33);
            }
        });

        colorButton[34].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(34);
            }
        });

        colorButton[35].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(35);
            }
        });

        colorButton[36].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(36);
            }
        });

        colorButton[37].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(37);
            }
        });

        colorButton[38].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(38);
            }
        });

        colorButton[39].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(39);
            }
        });

        colorButton[40].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(40);
            }
        });

        colorButton[41].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(41);
            }
        });

        colorButton[42].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(42);
            }
        });

        colorButton[43].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(43);
            }
        });

        colorButton[44].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(44);
            }
        });

        colorButton[45].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(45);
            }
        });

        colorButton[46].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(46);
            }
        });

        colorButton[47].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(47);
            }
        });

        colorButton[48].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnColorCreateActivity(48);
            }
        });
        /************************************************/

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

