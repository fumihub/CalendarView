package com.non_name_hero.calenderview.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.non_name_hero.calenderview.R;
import com.non_name_hero.calenderview.inputForm.InputActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//フラグメントにカレンダーのビューを表示させる
public class CalendarPageFragment extends Fragment{

    private Intent intent;
    private CalendarAdapter mCalendarAdapter;
    private GridView calendarGridView;
    private int mProgressMonth;
    private List<Date> dateArray;
    private CalendarViewModel mCalendarViewModel;
    private Map<String, String> mCalendarMap = new HashMap<String, String>();
    private Boolean hasBeenHandled = Boolean.FALSE;

    //コンストラクタ
    public CalendarPageFragment(int progressMonth){
        mProgressMonth = progressMonth;
    }

    //onCreateViewは戻り値のビューを表示させる
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //表示させるViewを指定
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.calendar_fragment_screen_slide_page, container, false);
        mCalendarViewModel = MainActivity.obtainViewModel(requireActivity());
        //カレンダのIDを取得
        calendarGridView = rootView.findViewById(R.id.calendarGridView);
        mCalendarAdapter = new CalendarAdapter(getContext(), mProgressMonth);
        mCalendarViewModel.getSchedules().observe(getViewLifecycleOwner(), new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> stringStringMap) {
                if (hasBeenHandled == Boolean.FALSE) {
                    hasBeenHandled = Boolean.TRUE;
                    mCalendarMap = stringStringMap;
                    mCalendarAdapter.setCalendarMap(mCalendarMap);
                    calendarGridView.setAdapter(mCalendarAdapter);
                }
            }
        });

        //カレンダーのアダプターを使用してViewを作成-

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
                //年月日のフォーマット
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.US);
                SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.US);
                SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.US);
                //選択されたセルのViewIdを取得
                TextView selectedDateText =(TextView) view.findViewById(R.id.dateText);
                //トーストメッセージ作成
                String message = selectedDateText.getText().toString();
                //トーストを表示
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                //入力画面に遷移
                intent = new Intent(getContext(), InputActivity.class);
                final List<Date> dateArray = mCalendarAdapter.getDateArray();
                //入力画面に引数で年月日を渡す
                intent.putExtra("year", yearFormat.format(dateArray.get(position)));
                intent.putExtra("month", monthFormat.format(dateArray.get(position)));
                intent.putExtra("day", message);
                startActivity(intent);
            }
        });

        calendarGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO スケジュール詳細画面に遷移
                return true;
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}