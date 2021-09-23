package com.non_name_hero.calenderview.calendar

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationView
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.databinding.ActivityMainBinding
import com.non_name_hero.calenderview.inputForm.SettingActivity
import com.non_name_hero.calenderview.utils.ActivityUtils
import com.non_name_hero.calenderview.utils.obtainViewModel
import androidx.drawerlayout.widget.DrawerLayout
import android.view.Menu
import android.util.Log
import androidx.core.view.GravityCompat





class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    /*TODO accountingTextをどこに実装したらいいかわからない
    *  　　家計簿画面の時のみ表示したい*/
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdView: AdView
    private lateinit var pigIconButton: ImageButton             /*家計簿画面切り替えボタン*/
    private lateinit var calendarIconButton: ImageButton        /*スケジュール画面切り替えボタン*/
    private var createFlag = false                              /*画面作成時フラグ*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        //DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

//        /*スケジュール画面では表示しない*/
//        accountingText = findViewById(R.id.accounting)
//        accountingText.visibility = View.GONE

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

        /*スケジュール→家計簿切り替えボタン設定********/
        pigIconButton = findViewById(R.id.pigIconButton)
        calendarIconButton = findViewById(R.id.calendarIconButton)
//        calendarIconButton.visibility = View.GONE
        pigIconButton.setOnClickListener {
            /*カレンダー画面に家計簿内容表示*/
//            pigIconButton.visibility = View.GONE
//            calendarIconButton.visibility = View.VISIBLE
//            changeMode(true)
            binding.viewmodel?.setCurrentMode(false)

        }
        calendarIconButton.setOnClickListener {
            /*カレンダー画面にスケジュール内容表示*/
//            calendarIconButton.visibility = View.GONE
//            pigIconButton.visibility = View.VISIBLE
//            changeMode(false)
            binding.viewmodel?.setCurrentMode(true)

        }
        /************************************************/

        //Toolbarをセット
        val myToolbar = findViewById<View>(R.id.mainToolbar) as Toolbar
        setSupportActionBar(binding.mainToolbar)

        //bindingを即時反映
        binding.executePendingBindings()

        /*balanceFlag判定用Flag*/
        createFlag = true

        /*DrawerToggle*/
        val drawer = findViewById<View>(R.id.drawerLayout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, myToolbar,
                R.string.drawer_open,
                R.string.drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        /*NavigationView Listener*/
        val navigationView = findViewById<View>(R.id.navigationView) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

    }

    /*NavigationView用関数*********************/
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            /*カレンダーボタンが押された時*/
            R.id.schedule -> {
                /*カレンダーにスケジュール表示*/
                binding.viewmodel?.setCurrentMode(true)
            }
            /*家計簿ボタンが押された時*/
            R.id.balance -> {
                /*カレンダーに家計簿表示*/
                binding.viewmodel?.setCurrentMode(false)
            }
            /*家計簿サマリーボタンが押された時*/
            R.id.balanceSummary -> {
                Log.d(ContentValues.TAG, "balanceSummary Selected!")
            }
            /*設定ボタンが押された時*/
            R.id.setting -> {
                Log.d(ContentValues.TAG, "setting Selected!")
                /*設定画面遷移*/
                goSettingActivity()
            }
        }

        val drawer = findViewById<View>(R.id.drawerLayout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
    /************************************************/

    /*設定画面遷移関数*********************/
    private fun goSettingActivity() {
        /*設定画面遷移用intent*/
        val intentOut = Intent(this, SettingActivity::class.java)
        startActivityForResult(intentOut, REQUEST_CODE)
    }
    /************************************************/

    /*画面表示時処理関数*******************************/
    public override fun onResume() {
        super.onResume()

//        /*createFlagがTRUEならば*/
//        if (createFlag) {
//            /*アプリ再開時にbalanceFlagを0にする*/
//            /*カレンダー画面にスケジュール内容表示*/
//            calendarIconButton.visibility = View.GONE
//            pigIconButton.visibility = View.VISIBLE
//            changeMode(false)
////            accountingText.visibility = View.GONE
//        } else {
//            /* 何もしない */
//        }
    }
    /************************************************/

    /*編集モードかを判定する関数********************/
    private fun changeMode(value: Boolean) {
//        /*SharedPreferenceでeditFlagの値を変更*/
//        val prefs = getSharedPreferences("input_balance_data", MODE_PRIVATE)
//        val editor = prefs.edit()
//        /*SharedPreferenceにbalanceFlagの値を保存*/
//        editor.putBoolean("balanceFlag", value)
//        /*非同期処理ならapply()、同期処理ならcommit()*/
//        editor.apply()

    }
    /************************************************/

    /**
     * ViewModelを取得する
     * (this.obtainViewModel(Class: ViewModel)は拡張関数)
     * @return viewModel {CalendarViewModel} カレンダー関連の情報を保持するViewModel
     */
    fun obtainViewModel(): CalendarViewModel = this.obtainViewModel(CalendarViewModel::class.java)

    /*定数定義****************************************/
    companion object {
        private const val REQUEST_CODE = 1
    }
    /************************************************/
}