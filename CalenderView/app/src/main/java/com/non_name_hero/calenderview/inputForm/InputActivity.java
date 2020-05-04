package com.non_name_hero.calenderview.inputForm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.non_name_hero.calenderview.R;

public class InputActivity extends AppCompatActivity {

    private EditText title;
    private EditText time;
    private TextView color;
    private EditText myBudget;
    private EditText price;
    private TextView place;
    private EditText memo;
    private TextView picture;

    private Button detailButton;
    private Button cancelButton;
    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.input_main);

        Intent intent = getIntent();

        final Toolbar myToolbar = (Toolbar) findViewById(R.id.inputToolbar);
        setSupportActionBar(myToolbar);

        /*入力画面表示*********************************************************************/
        //カレンダーセルのボタンが押された場合
        title = findViewById(R.id.title);
        time = findViewById(R.id.time);
        color = findViewById(R.id.color);
        detailButton = findViewById(R.id.detailButton);
        myBudget = findViewById(R.id.myBudget);
        price = findViewById(R.id.price);
        place = findViewById(R.id.place);
        memo = findViewById(R.id.memo);
        picture = findViewById(R.id.picture);
        cancelButton = findViewById(R.id.cancelButton);
        doneButton = findViewById(R.id.doneButton);

        //最初表示
        title.setVisibility(View.VISIBLE);
        time.setVisibility(View.VISIBLE);
        color.setVisibility(View.VISIBLE);
        detailButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
        doneButton.setVisibility(View.VISIBLE);
        //最初非表示
        myBudget.setVisibility(View.GONE);
        price.setVisibility(View.GONE);
        place.setVisibility(View.GONE);
        memo.setVisibility(View.GONE);
        picture.setVisibility(View.GONE);

        //詳細ボタンが押されたとき
        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //詳細入力表示に
                detailButton.setVisibility(View.GONE);
                myBudget.setVisibility(View.VISIBLE);
                price.setVisibility(View.VISIBLE);
                place.setVisibility(View.VISIBLE);
                memo.setVisibility(View.VISIBLE);
                picture.setVisibility(View.VISIBLE);
            }
        });

        //完了ボタンが押されたとき
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //カレンダー表示画面に遷移

            }
        });

        //キャンセルボタンが押されたとき
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //カレンダー表示画面に遷移
                finish();
            }
        });

        /*********************************************************************************/

    }
}
