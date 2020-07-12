package com.non_name_hero.calenderview.inputForm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.non_name_hero.calenderview.R;

public class colorSelectActivity extends AppCompatActivity {

    private Button noCategoryButton;
    private Button colorCreateButton;

    private Intent intentOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.color_select);
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.colorSelectToolbar);
        setSupportActionBar(myToolbar);

        //色作成画面用intent
        intentOut = new Intent(this, colorCreateActivity.class);

        noCategoryButton = findViewById(R.id.noCategoryButton);
        colorCreateButton = findViewById(R.id.colorCreateButton);

        /*未分類ボタンが押されたとき******************/
        noCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input画面に遷移
                finish();
            }
        });
        /************************************************/

        /*色作成ボタンが押されたとき************************/
        colorCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //色作成画面に遷移
                startActivity(intentOut);
            }
        });
        /************************************************/

    }
}
