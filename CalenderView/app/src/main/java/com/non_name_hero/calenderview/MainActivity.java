package com.non_name_hero.calenderview;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.TextView;
import android.os.Bundle;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    //private TextView titleText;
    private GridView calendarGridView;
    private CalendarAdapter calendarAdapter;
    private AdView mAdView;
    /*ボタン作成しないためコメントアウト
    private Button prevButton, nextButton;
    private CalendarAdapter mCalendarAdapter;
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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