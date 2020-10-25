package com.non_name_hero.calenderview.calendar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.GridView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.databinding.CalendarFragmentScreenSlidePageBinding
import com.non_name_hero.calenderview.inputForm.InputActivity
import com.non_name_hero.calenderview.utils.PigLeadUtils

/**
 * フラグメントにカレンダーを表示させるクラス
 */
class CalendarPageFragment     //コンストラクタ
(private val mProgressMonth: Int) : Fragment() {
    private var intent: Intent? = null
    private var mCalendarAdapter: CalendarAdapter? = null
    private var calendarGridView: GridView? = null
    private var mCalendarViewModel: CalendarViewModel? = null

    /**
     * onCreateViewは戻り値のビューを表示させる
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return 表示されるView
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: CalendarFragmentScreenSlidePageBinding
        //DataBinding
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.calendar_fragment_screen_slide_page,
                container,
                false)
        //CalendarViewModelを作成
        mCalendarViewModel = MainActivity.Companion.obtainViewModel(requireActivity())
        binding.viewmodel = mCalendarViewModel
        binding.lifecycleOwner = activity
        //カレンダーを作成(GridView)
        calendarGridView = binding.calendarGridView
        mCalendarAdapter = CalendarAdapter(context, mProgressMonth)
        calendarGridView!!.adapter = mCalendarAdapter

        //クリックリスナー
        calendarGridView!!.onItemClickListener = OnItemClickListener { parent, view, position, id ->

            /**
             * GridViewのクリックリスナー
             * @param parent 表示されているAdapterViewのインスタンス参照
             * @param view 選択されたItemのViewインスタンス参照
             * @param position GridView内でのポジション
             * @param id Adapter内メソッドgetItemIdで設定した値　ここではpositionをそのまま返してる
             */
            /**
             * GridViewのクリックリスナー
             * @param parent 表示されているAdapterViewのインスタンス参照
             * @param view 選択されたItemのViewインスタンス参照
             * @param position GridView内でのポジション
             * @param id Adapter内メソッドgetItemIdで設定した値　ここではpositionをそのまま返してる
             */
            val dateArray = mCalendarAdapter.getDateArray()
            mCalendarViewModel!!.setScheduleItem(dateArray!![position])
        }
        calendarGridView!!.onItemLongClickListener = OnItemLongClickListener { parent, view, position, id -> //入力画面に遷移
            intent = Intent(context, InputActivity::class.java)
            val dateArray = mCalendarAdapter.getDateArray()
            //入力画面に引数で年月日を渡す
            intent!!.putExtra("year", PigLeadUtils.yearFormat.format(dateArray!![position]))
            intent!!.putExtra("month", PigLeadUtils.monthFormat.format(dateArray!![position]))
            intent!!.putExtra("day", PigLeadUtils.dayFormat.format(dateArray!![position]))
            startActivity(intent)
            true
        }
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}