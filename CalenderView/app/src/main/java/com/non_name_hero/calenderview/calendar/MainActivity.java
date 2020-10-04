package com.non_name_hero.calenderview.calendar;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.non_name_hero.calenderview.R;
import com.non_name_hero.calenderview.databinding.ActivityMainBinding;
import com.non_name_hero.calenderview.utils.ViewModelFactory;

import static com.non_name_hero.calenderview.utils.ActivityUtils.addFragmentToActivity;

public class MainActivity extends AppCompatActivity {

    private TextView accountingText;
    private AdView mAdView;
    private CalendarViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        //DataBinding
        ActivityMainBinding binding;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

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
        CalendarFragment calendarFragment = (CalendarFragment) getSupportFragmentManager().findFragmentById(R.id.calendar_fragment_container);
        if (calendarFragment == null) {
            calendarFragment = CalendarFragment.newInstance();
            addFragmentToActivity(getSupportFragmentManager(), calendarFragment, R.id.calendar_fragment_container);
        }

        //ViewModelの参照を取得
        mViewModel = obtainViewModel(this);
        //ViewModelをbinding
        binding.setViewmodel(mViewModel);
        //LifecycleOwnerを指定
        binding.setLifecycleOwner(this);
        //Toolbarをセット
        setSupportActionBar(binding.mainToolbar);
        binding.mainToolbar.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.toolbar));

        //bindingを即時反映
        binding.executePendingBindings();
    }

    /**
     * ViewModelを取得する
     *
     * @param activity ViewModelライフサイクルオーナーとしてのアクティビティ
     * @return viewModel {CalendarViewModel} カレンダー関連の情報を保持するViewModel
     */
    public static CalendarViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
        return new ViewModelProvider(activity, factory).get(CalendarViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}