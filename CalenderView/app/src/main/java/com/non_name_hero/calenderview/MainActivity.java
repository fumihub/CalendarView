package com.non_name_hero.calenderview;


import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {

    //private TextView titleText;
    //private Button prevButton, nextButton;
    private CalendarAdapter mCalendarAdapter;
    private GridView calendarGridView;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ここで1秒間スリープし、スプラッシュを表示させたままにする。
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        // スプラッシュthemeを通常themeに変更する
        setTheme(R.style.AppTheme);

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

/*      ボタンとタイトルは表示させないためコメントアウト
        titleText = findViewById(R.id.titleText);
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
        mCalendarAdapter = new CalendarAdapter(this);
        calendarGridView.setAdapter(mCalendarAdapter);
        //titleText.setText(mCalendarAdapter.getTitle());
    }

//    private boolean isScrolling() {
//        float scrolledX = Math.abs(accumulatedScrollOffset.x);
//        int expectedScrollX = Math.abs(width * monthsScrolledSoFar);
//        return scrolledX < expectedScrollX - 5 || scrolledX > expectedScrollX + 5;
//    }
//
//    boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        //ignore scrolling callback if already smooth scrolling
//        if (isSmoothScrolling) {
//            return true;
//        }
//
//        if (currentDirection == CompactCalendarController.Direction.NONE) {
//            if (Math.abs(distanceX) > Math.abs(distanceY)) {
//                currentDirection = CompactCalendarController.Direction.HORIZONTAL;
//            } else {
//                currentDirection = CompactCalendarController.Direction.VERTICAL;
//            }
//        }
//
//        isScrolling = true;
//        this.distanceX = distanceX;
//        return true;
//    }

}