package com.non_name_hero.calenderview.inputForm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.non_name_hero.calenderview.R;

public class colorCreateActivity extends AppCompatActivity {

    private EditText colorCreateTitle;

    private Button color1;
    private Button color2;
    private Button cancelButton;
    private Button doneButton;

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

        /*色ボタンが押されたとき******************/
        color1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //色画面に遷移
                startActivity(intentOut);
            }
        });
        color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //色画面に遷移
                startActivity(intentOut);
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
}
