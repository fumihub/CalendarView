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
import java.util.*
import kotlin.collections.ArrayList

/**
 * フラグメントにカレンダーを表示させるクラス
 */
class CalendarPageFragment     //コンストラクタ
(private val mProgressMonth: Int) : Fragment() {

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
        binding = CalendarFragmentScreenSlidePageBinding.inflate(
                inflater,
                container,
                false).apply() {
            this.lifecycleOwner = viewLifecycleOwner
            this.viewmodel = (activity as MainActivity).obtainViewModel()
        }

        //カレンダーを作成(GridView)
        binding.calendarGridView.adapter = CalendarAdapter(requireContext(), mProgressMonth)

        //クリックリスナー
        @Suppress("UNUSED_PARAMETER")
        binding.calendarGridView.onItemClickListener = OnItemClickListener { _, _, position, _ ->


            /**
             * GridViewのクリックリスナー
             * @param parent 表示されているAdapterViewのインスタンス参照
             * @param view 選択されたItemのViewインスタンス参照
             * @param position GridView内でのポジション
             * @param id Adapter内メソッドgetItemIdで設定した値　ここではpositionをそのまま返してる
             */
            val dateArray = (binding.calendarGridView.adapter as CalendarAdapter).dateArray
            binding.viewmodel?.setScheduleItem(dateArray[position])
        }
        binding.calendarGridView.onItemLongClickListener = OnItemLongClickListener { _, _, position, _ -> //入力画面に遷移
            val intent = Intent(context, InputActivity::class.java)
            val dateArray = (binding.calendarGridView.adapter as CalendarAdapter).dateArray
//            val dateArray = mCalendarAdapter.dateArray ?: ArrayList<Date>()
            /*TODO スケジュール入力時と家計簿入力時に分ける*/
            //入力画面に引数で年月日を渡す
            val year:Int = Integer.valueOf(PigLeadUtils.yearFormat.format(dateArray[position]))
            val month:Int = Integer.valueOf(PigLeadUtils.monthFormat.format(dateArray[position]))
            val day:Int = Integer.valueOf(PigLeadUtils.dayFormat.format(dateArray[position]))
            intent.putExtra("year", year)
            intent.putExtra("month", month)
            intent.putExtra("day", day)
            startActivity(intent)
            true
        }
        binding.executePendingBindings()
        return binding.root
    }

}