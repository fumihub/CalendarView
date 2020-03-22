package com.non_name_hero.calenderview;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.TextView;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //private TextView titleText;
    private GridView calendarGridView;
    private CalendarAdapter calendarAdapter;
    /*ボタン作成しないためコメントアウト
    private Button prevButton, nextButton;
    private CalendarAdapter mCalendarAdapter;
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //titleText = findViewById(R.id.titleText);
        /*ボタン作成しないためコメントアウト
        prevButton = findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarAdapter.prevMonth();
                titleText.setText(mCalendarAdapter.getTitle());
            }
        });
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarAdapter.nextMonth();
                titleText.setText(mCalendarAdapter.getTitle());
            }
        });
         */
        calendarGridView = findViewById(R.id.calendarGridView);
        calendarAdapter = new CalendarAdapter(this);
        calendarGridView.setAdapter(calendarAdapter);
        //titleText.setText(calendarAdapter.getTitle());
    }

}