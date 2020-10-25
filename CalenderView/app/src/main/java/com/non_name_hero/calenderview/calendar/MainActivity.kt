package com.non_name_hero.calenderview.calendar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.databinding.ActivityMainBinding
import com.non_name_hero.calenderview.utils.ActivityUtils
import com.non_name_hero.calenderview.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private var accountingText: TextView? = null
    private var mAdView: AdView? = null
    private var mViewModel: CalendarViewModel? = null
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
        mAdView.loadAd(adRequest)

        //アクティビティのfragmentを取得（初回時はnull）
        var calendarFragment = supportFragmentManager.findFragmentById(R.id.calendar_fragment_container) as CalendarFragment?
        if (calendarFragment == null) {
            calendarFragment = CalendarFragment.Companion.newInstance()
            ActivityUtils.addFragmentToActivity(supportFragmentManager, calendarFragment, R.id.calendar_fragment_container)
        }

        //ViewModelの参照を取得
        mViewModel = obtainViewModel(this)
        //ViewModelをbinding
        binding.viewmodel = mViewModel
        //LifecycleOwnerを指定
        binding.lifecycleOwner = this
        // ViewModelを監視して変更があればToolbarの色を更新する
        mViewModel!!.currentMonth.observe(this, { integer ->
            val colors = resources.obtainTypedArray(R.array.toolbar_color_array)
            binding.mainToolbar.setBackgroundColor(colors.getColor(integer - 1, resources.getColor(R.color.toolbar_string)))
        })
        //Toolbarをセット
        setSupportActionBar(binding.mainToolbar)

        //bindingを即時反映
        binding.executePendingBindings()
    }

    public override fun onResume() {
        super.onResume()
    }

    companion object {
        /**
         * ViewModelを取得する
         *
         * @param activity ViewModelライフサイクルオーナーとしてのアクティビティ
         * @return viewModel {CalendarViewModel} カレンダー関連の情報を保持するViewModel
         */
        fun obtainViewModel(activity: FragmentActivity?): CalendarViewModel {
            val factory: ViewModelFactory = ViewModelFactory.Companion.getInstance(activity!!.application)
            return ViewModelProvider(activity, factory).get(CalendarViewModel::class.java)
        }
    }
}