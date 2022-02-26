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
import com.non_name_hero.calenderview.databinding.CalendarFragmentBinding
import com.non_name_hero.calenderview.utils.ActivityUtils
import java.lang.Boolean

class CalendarFragment : Fragment() {
    private var mPager: ViewPager2? = null
    private var mPagerAdapter: CalendarPagerAdapter? = null
    private lateinit var binding: CalendarFragmentBinding
    private var pagerIdleFlag = Boolean.FALSE
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        this.binding = CalendarFragmentBinding.inflate(inflater, container, false).apply {
            viewmodel = (activity as MainActivity).obtainViewModel()
            lifecycleOwner = viewLifecycleOwner
        }
        // ViewPagerをセットアップ
        initPager(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //データを更新
        loadData()
        //予定一覧を更新
        initScheduleList()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        // カレンダーのスケジュールを更新
        binding.viewmodel?.reloadCalendarData(true)
    }

    private fun loadData() {
        binding.viewmodel?.start()
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
        mPager!!.offscreenPageLimit = 2
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
                if (pagerIdleFlag) {
                    val offsetMonth = position - DEFAULT_PAGE
                    binding.viewmodel?.setCurrentYearMonth(offsetMonth)
                }
            }
        })
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private inner class CalendarPagerAdapter(f: Fragment?) : FragmentStateAdapter(f!!) {
        override fun createFragment(position: Int): Fragment {
            return CalendarPageFragment()
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