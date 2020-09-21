package com.non_name_hero.calenderview.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.non_name_hero.calenderview.R;
import com.non_name_hero.calenderview.databinding.CalendarFragmentScreenSlidePageBinding;
import com.non_name_hero.calenderview.inputForm.InputActivity;
import com.non_name_hero.calenderview.utils.PigLeadUtils;

import java.util.Date;
import java.util.List;

/**
 * フラグメントにカレンダーを表示させるクラス
 */
public class CalendarPageFragment extends Fragment {

    private Intent intent;
    private CalendarAdapter mCalendarAdapter;
    private GridView calendarGridView;
    private int mProgressMonth;
    private CalendarViewModel mCalendarViewModel;

    //コンストラクタ
    public CalendarPageFragment(int progressMonth) {
        mProgressMonth = progressMonth;
    }

    /**
     * onCreateViewは戻り値のビューを表示させる
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return 表示されるView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CalendarFragmentScreenSlidePageBinding binding;
        //DataBinding
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.calendar_fragment_screen_slide_page,
                container,
                false);
        //CalendarViewModelを作成
        mCalendarViewModel = MainActivity.obtainViewModel(requireActivity());
        binding.setViewmodel(mCalendarViewModel);
        binding.setLifecycleOwner(getActivity());
        //カレンダーを作成(GridView)
        calendarGridView = binding.calendarGridView;
        mCalendarAdapter = new CalendarAdapter(getContext(), mProgressMonth);
        calendarGridView.setAdapter(mCalendarAdapter);

        //クリックリスナー
        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * GridViewのクリックリスナー
             * @param parent 表示されているAdapterViewのインスタンス参照
             * @param view 選択されたItemのViewインスタンス参照
             * @param position GridView内でのポジション
             * @param id Adapter内メソッドgetItemIdで設定した値　ここではpositionをそのまま返してる
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO スケジュール詳細画面に遷移
            }
        });

        calendarGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //入力画面に遷移
                intent = new Intent(getContext(), InputActivity.class);
                final List<Date> dateArray = mCalendarAdapter.getDateArray();
                //入力画面に引数で年月日を渡す
                intent.putExtra("year", PigLeadUtils.yearFormat.format(dateArray.get(position)));
                intent.putExtra("month", PigLeadUtils.monthFormat.format(dateArray.get(position)));
                intent.putExtra("day", PigLeadUtils.dayFormat.format(dateArray.get(position)));
                startActivity(intent);
                return true;
            }
        });
        binding.executePendingBindings();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}