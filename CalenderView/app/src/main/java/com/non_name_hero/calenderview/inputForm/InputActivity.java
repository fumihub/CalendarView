package com.non_name_hero.calenderview.inputForm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.non_name_hero.calenderview.R;

import java.util.Calendar;

public class InputActivity extends AppCompatActivity {

    private EditText title;
    private EditText startDate;
    private EditText endDate;
    private EditText startTime;
    private EditText endTime;
    private TextView color;
    private EditText myBudget;
    private EditText price;
    private TextView place;
    private EditText memo;
    private TextView picture;

    private Button timeButton;
    private Button detailButton;
    private Button cancelButton;
    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.input_main);

        Intent intent = getIntent();
        /*int year = Integer.valueOf(intent.getStringExtra("year"));
        int month = Integer.valueOf(intent.getStringExtra("month"));
        int day = Integer.valueOf(intent.getStringExtra("day"));*/

        final Toolbar myToolbar = (Toolbar) findViewById(R.id.inputToolbar);
        setSupportActionBar(myToolbar);

        /*入力画面表示*********************************************************************/
        //カレンダーセルのボタンが押された場合
        title = findViewById(R.id.title);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        timeButton = findViewById(R.id.timeButton);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
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
        startDate.setVisibility(View.VISIBLE);
        endDate.setVisibility(View.VISIBLE);
        timeButton.setVisibility(View.VISIBLE);
        color.setVisibility(View.VISIBLE);
        detailButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
        doneButton.setVisibility(View.VISIBLE);
        //最初非表示
        startTime.setVisibility(View.GONE);
        endTime.setVisibility(View.GONE);
        myBudget.setVisibility(View.GONE);
        price.setVisibility(View.GONE);
        place.setVisibility(View.GONE);
        memo.setVisibility(View.GONE);
        picture.setVisibility(View.GONE);

        /*タイトルEditTextが押されたとき********************/
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //フォーカスを当てる
                title.setFocusable(true);
                title.setFocusableInTouchMode(true);
            }
        });
        /**************************************************/

        /*開始日時EditTextが押されたとき********************/
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calendarインスタンスを取得
                final Calendar startCalendar = Calendar.getInstance();

                //DatePickerDialogインスタンスを取得
                DatePickerDialog startDatePickerDialog = new DatePickerDialog(
                        InputActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //setした日付を取得して表示
                                startDate.setText(String.format("%02d / %02d", month+1, dayOfMonth));
                            }
                        },
                        startCalendar.get(Calendar.YEAR),
                        startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.DATE)
                );

                //dialogを表示
                startDatePickerDialog.show();

            }
        });
        /**************************************************/

        /*終了日時EditTextが押されたとき********************/
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calendarインスタンスを取得
                final Calendar endCalendar = Calendar.getInstance();

                //DatePickerDialogインスタンスを取得
                DatePickerDialog endDatePickerDialog = new DatePickerDialog(
                        InputActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //setした日付を取得して表示
                                endDate.setText(String.format("%02d / %02d", month+1, dayOfMonth));
                            }
                        },
                        endCalendar.get(Calendar.YEAR),
                        endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DATE)
                );

                //dialogを表示
                endDatePickerDialog.show();

            }
        });
        /**************************************************/

        /*時間入力ボタンが押されたとき********************/
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //詳細入力表示に
                timeButton.setVisibility(View.GONE);
                startTime.setVisibility(View.VISIBLE);
                endTime.setVisibility(View.VISIBLE);
            }
        });
        /************************************************/

        /*開始時間EditTextが押されたとき********************/
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calendarインスタンスを取得
                final Calendar startTimeCalendar = Calendar.getInstance();

                //TimePickerDialogインスタンスを取得
                TimePickerDialog startTimePickerDialog = new TimePickerDialog(
                        InputActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //setした時間を取得して表示
                                startTime.setText(String.format("%02d : %02d", hourOfDay, minute));
                            }
                        },
                        startTimeCalendar.get(Calendar.HOUR_OF_DAY),
                        startTimeCalendar.get(Calendar.MINUTE),
                        true
                );

                //dialogを表示
                startTimePickerDialog.show();

            }
        });
        /**************************************************/

        /*終了時間EditTextが押されたとき********************/
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calendarインスタンスを取得
                final Calendar endTimeCalendar = Calendar.getInstance();

                //TimePickerDialogインスタンスを取得
                TimePickerDialog endTimePickerDialog = new TimePickerDialog(
                        InputActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //setした時間を取得して表示
                                endTime.setText(String.format("%02d : %02d", hourOfDay, minute));
                            }
                        },
                        endTimeCalendar.get(Calendar.HOUR_OF_DAY),
                        endTimeCalendar.get(Calendar.MINUTE),
                        true
                );

                //dialogを表示
                endTimePickerDialog.show();

            }
        });
        /**************************************************/

        /*詳細ボタンが押されたとき************************/
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
        /************************************************/

        /*完了ボタンが押されたとき************************/
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //カレンダー表示画面に遷移

            }
        });
        /************************************************/

        /*キャンセルボタンが押されたとき******************/
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //カレンダー表示画面に遷移
                finish();
            }
        });
        /************************************************/

        /*********************************************************************************/

    }
}
