package com.non_name_hero.calenderview.calendar

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.databinding.ActivityMainBinding
import com.non_name_hero.calenderview.utils.ActivityUtils
import com.non_name_hero.calenderview.utils.obtainViewModel

class MainActivity : AppCompatActivity() {
    private var accountingText: TextView? = null
    private var mAdView: AdView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        //DataBinding
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //収入支出ブロックの背景色変更
        accountingText = findViewById(R.id.accounting)
        //accountingText.setBackgroundColor(Color.YELLOW);

        //広告の読み込み
        MobileAds.initialize(this) { }

        //UI部分のバナー広告とIDを接続
        mAdView = findViewById(R.id.adView)
        //インスタンス生成
        val adRequest = AdRequest.Builder().build()
        //広告読み込み
        mAdView?.loadAd(adRequest)

        //アクティビティのfragmentを取得（初回時はnull）
        var calendarFragment = supportFragmentManager.findFragmentById(R.id.calendar_fragment_container) as CalendarFragment?
        if (calendarFragment == null) {
            calendarFragment = CalendarFragment.Companion.newInstance()
            ActivityUtils.addFragmentToActivity(supportFragmentManager, calendarFragment, R.id.calendar_fragment_container)
        }

        //LifecycleOwnerを指定
        binding.lifecycleOwner = this
        binding.apply {
            this.viewmodel = obtainViewModel()
            // ViewModelを監視して変更があればToolbarの色を更新する

            val colors = resources.obtainTypedArray(R.array.toolbar_color_array)
            this.viewmodel?.let{
                it.currentMonth.observe(lifecycleOwner as MainActivity) { integer ->
                    if (integer != null) {
                        binding.mainToolbar.setBackgroundColor(ContextCompat.getColor(applicationContext,colors.getResourceId(integer-1, 0)))
                    }
                }
            }
        }

        /*TODO ツールバーにスケジュール入力と家計簿入力を切り替えるボタン追加*/
        //Toolbarをセット
        setSupportActionBar(binding.mainToolbar)

        //bindingを即時反映
        binding.executePendingBindings()
    }

    public override fun onResume() {
        super.onResume()
    }

    /**
     * ViewModelを取得する
     * (this.obtainViewModel(Class: ViewModel)は拡張関数)
     * @return viewModel {CalendarViewModel} カレンダー関連の情報を保持するViewModel
     */
    fun obtainViewModel(): CalendarViewModel = this.obtainViewModel(CalendarViewModel::class.java)
}