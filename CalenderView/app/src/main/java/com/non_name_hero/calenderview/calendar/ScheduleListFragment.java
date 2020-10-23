package com.non_name_hero.calenderview.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.non_name_hero.calenderview.R;
import com.non_name_hero.calenderview.databinding.ScheduleFragmentBinding;

public class ScheduleListFragment extends Fragment {


    public static ScheduleListFragment newInstance() {
        return new ScheduleListFragment();
    }

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CalendarViewModel viewModel;
    private ScheduleListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //viewの生成
        ScheduleFragmentBinding binding;
        binding = DataBindingUtil.inflate(inflater, R.layout.schedule_fragment, container, false);
        //CalendarViewModelを取得
        viewModel = MainActivity.obtainViewModel(getActivity());
        recyclerView = binding.scheduleRecyclerView;
        // RecyclerViewの設定
        recyclerView.setHasFixedSize(true);// アイテムの大きさが固定ならtrue
        adapter = new ScheduleListAdapter(getContext(), viewModel);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        // setItemTouchHelperの設定
        ItemTouchHelper itemDecor = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        final int toPos = target.getAdapterPosition();
                        adapter.notifyItemMoved(fromPos, toPos);
                        return true;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        adapter.removeScheduleItem(fromPos);
                    }
                });
        itemDecor.attachToRecyclerView(binding.scheduleRecyclerView);
        binding.setViewmodel(viewModel);
        binding.setLifecycleOwner(getActivity());
        return binding.getRoot();
    }

}
