package com.non_name_hero.calenderview.calendar;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.non_name_hero.calenderview.R;

public class MainActivity extends AppCompatActivity {

    private TextView titleText;
    private TextView accountingText;
    private Button prevButton, nextButton, scheduleButton;
    private CalendarAdapter mCalendarAdapter;
    private GridView calendarGridView;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ここで1秒間スリープし、スプラッシュを表示させたままにする。
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        // スプラッシュthemeを通常themeに変更する
        setTheme(R.style.AppTheme);

        setContentView(R.layout.activity_main);

        final Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        titleText = findViewById(R.id.titleText);
        prevButton = findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarAdapter.prevMonth();
                titleText.setText(mCalendarAdapter.getTitle());
                myToolbar.setTitle(mCalendarAdapter.getTitle());
            }
        });
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarAdapter.nextMonth();
                titleText.setText(mCalendarAdapter.getTitle());
                myToolbar.setTitle(mCalendarAdapter.getTitle());
            }
        });

        //収入支出ブロックの背景色変更
        accountingText = findViewById(R.id.accounting);
        accountingText.setBackgroundColor(Color.YELLOW);

//        scheduleButton = findViewById(R.id.clearButton);
//        scheduleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                accountingText.setText("aaaa");
//            }
//        });

        //広告の読み込み
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        //UI部分のバナー広告とIDを接続
        mAdView = findViewById(R.id.adView);
        //インスタンス生成
        AdRequest adRequest = new AdRequest.Builder().build();
        //広告読み込み
        mAdView.loadAd(adRequest);

        calendarGridView = findViewById(R.id.calendarGridView);
        mCalendarAdapter = new CalendarAdapter(this);
        calendarGridView.setAdapter(mCalendarAdapter);
        titleText.setText(mCalendarAdapter.getTitle());
        myToolbar.setTitle(mCalendarAdapter.getTitle());
    }

}