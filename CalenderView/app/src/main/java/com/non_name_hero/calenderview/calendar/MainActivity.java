package com.non_name_hero.calenderview.calendar;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.non_name_hero.calenderview.R;
import com.non_name_hero.calenderview.data.source.ScheduleRepository;
import com.non_name_hero.calenderview.utils.Injection;

import java.util.Date;

import static com.non_name_hero.calenderview.utils.ActivityUtils.addFragmentToActivity;

public class MainActivity extends AppCompatActivity {



    private CalendarPresenter mPresenter;
    private Date mCurrentDate;
    private TextView titleText;
    private TextView accountingText;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ここで1秒間スリープし、スプラッシュを表示させたままにする。
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//        }
        // スプラッシュthemeを通常themeに変更する
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);


        final Toolbar myToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(myToolbar);
          /*
       TODO タイトル表示
        titleText.setText(mCalendarAdapter.getTitle());
        myToolbar.setTitle(mCalendarAdapter.getTitle());
        */

        //収入支出ブロックの背景色変更
        accountingText = findViewById(R.id.accounting);
       //accountingText.setBackgroundColor(Color.YELLOW);

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

        //アクティビティのfragmentを取得（初回時はnull）

        CalendarFragment calendarFragment = (CalendarFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(calendarFragment == null) {
            calendarFragment = CalendarFragment.newInstance();
            addFragmentToActivity(getSupportFragmentManager(), calendarFragment, R.id.fragment_container);
        }

        ScheduleRepository scheduleRepository = Injection.provideScheduleRepository(getApplicationContext());

        new CalendarPresenter(calendarFragment ,scheduleRepository);


    }




}