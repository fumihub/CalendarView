package com.non_name_hero.calenderview.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.utils.ActivityUtils
import java.lang.Boolean
import java.util.*

class CalendarFragment : Fragment() {
    private var mPager: ViewPager2? = null
    private var mPagerAdapter: CalendarPagerAdapter? = null
    private var mViewModel: CalendarViewModel? = null
    private var mCalendar: Calendar? = null
    private var pagerIdleFlag = Boolean.FALSE
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mCalendar = Calendar.getInstance()
        val rootView = inflater.inflate(R.layout.calendar_fragment, container, false)
        // ViewPagerをセットアップ
        initPager(rootView)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initScheduleList()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //CalendarViewModelを取得
        mViewModel = MainActivity.Companion.obtainViewModel(activity)
        //データを更新
        loadData()
    }

    override fun onResume() {
        super.onResume()
        // カレンダーのスケジュールを更新
        mViewModel!!.reloadCalendarData(true)
    }

    private fun loadData() {
        mViewModel!!.start()
    }

    private fun getCurrentMonth(position: Int): Int {
        val nowMonth = mCalendar!![Calendar.MONTH] // 0~11
        val offset = position - DEFAULT_PAGE
        //offset -> 現在ページからの差分
        //monthOffset -> 現在月からの差分
        var monthOffset: Int
        monthOffset = Math.abs(offset) % MAX_MONTH
        //マイナスの場合は12から差をとる
        if (offset < 0) monthOffset = MAX_MONTH - monthOffset
        // n月からの差分[-n 〜 n] + n % 12 + 1 → 1~12
        return (monthOffset + nowMonth) % MAX_MONTH + 1
    }

    private fun initScheduleList() {
        //ScheduleListを表示
        var scheduleListFragment = childFragmentManager.findFragmentById(R.id.schedule_list_fragment_container) as ScheduleListFragment?
        if (scheduleListFragment == null) {
            scheduleListFragment = ScheduleListFragment.Companion.newInstance()
            ActivityUtils.addFragmentToActivity(childFragmentManager, scheduleListFragment, R.id.schedule_list_fragment_container)
        }
    }

    // ViewPager2を初期設定
    private fun initPager(rootView: View) {
        // pagerを設定
        mPager = rootView.findViewById<View>(R.id.pager) as ViewPager2
        mPagerAdapter = CalendarPagerAdapter(this)
        mPager!!.offscreenPageLimit = 5
        mPager!!.adapter = mPagerAdapter
        mPager!!.setCurrentItem(DEFAULT_PAGE, false)
        mPager!!.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    //IDLEフラグをTRUEにする
                    pagerIdleFlag = Boolean.TRUE
                }
            }

            override fun onPageSelected(position: Int) {
                //pagerがIDLE状態の場合、現在の月をviewModelにセット矢印DataBindingでtoolbarに表示
                if (pagerIdleFlag) mViewModel!!.setCurrentMonth(getCurrentMonth(position))
            }
        })
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private inner class CalendarPagerAdapter(f: Fragment?) : FragmentStateAdapter(f!!) {
        override fun createFragment(position: Int): Fragment {
            return CalendarPageFragment(position - DEFAULT_PAGE)
        }

        override fun getItemCount(): Int {
            return NUM_PAGES
        }
    }

    companion object {
        fun newInstance(): CalendarFragment {
            return CalendarFragment()
        }

        private const val NUM_PAGES = 100
        private const val DEFAULT_PAGE = NUM_PAGES / 2
        private const val MAX_MONTH = 12
    }
}