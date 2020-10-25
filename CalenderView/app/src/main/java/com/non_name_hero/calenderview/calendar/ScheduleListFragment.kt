package com.non_name_hero.calenderview.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.databinding.ScheduleFragmentBinding

class ScheduleListFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var viewModel: CalendarViewModel? = null
    private var adapter: ScheduleListAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //viewの生成
        val binding: ScheduleFragmentBinding
        binding = DataBindingUtil.inflate(inflater, R.layout.schedule_fragment, container, false)
        //CalendarViewModelを取得
        viewModel = MainActivity.Companion.obtainViewModel(activity)
        recyclerView = binding.scheduleRecyclerView
        // RecyclerViewの設定
        recyclerView!!.setHasFixedSize(true) // アイテムの大きさが固定ならtrue
        adapter = ScheduleListAdapter(context, viewModel)
        recyclerView!!.adapter = adapter
        layoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = layoutManager
        // setItemTouchHelperの設定
        val itemDecor = ItemTouchHelper(
                object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                        ItemTouchHelper.RIGHT) {
                    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                        val fromPos = viewHolder.adapterPosition
                        val toPos = target.adapterPosition
                        adapter!!.notifyItemMoved(fromPos, toPos)
                        return true
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val fromPos = viewHolder.adapterPosition
                        adapter!!.removeScheduleItem(fromPos)
                    }
                })
        itemDecor.attachToRecyclerView(binding.scheduleRecyclerView)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = activity
        return binding.root
    }

    companion object {
        fun newInstance(): ScheduleListFragment {
            return ScheduleListFragment()
        }
    }
}