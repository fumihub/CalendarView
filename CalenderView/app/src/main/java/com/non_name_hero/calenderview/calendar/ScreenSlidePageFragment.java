package com.non_name_hero.calenderview.calendar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.ListViewAutoScrollHelper;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.formats.NativeAdViewHolder;
import com.non_name_hero.calenderview.R;

//フラグメントにカレンダーのビューを表示させる
public class ScreenSlidePageFragment extends Fragment {

    private TextView titleText;
    private CalendarAdapter mCalendarAdapter;
    private GridView calendarGridView;

    //コンストラクタ
    public ScreenSlidePageFragment(){

    }
    //onCreateViewは戻り値のビューを表示させる
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //表示させるViewを指定
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        //カレンダのIDを取得
        calendarGridView = rootView.findViewById(R.id.calendarGridView);
        //カレンダーのアダプターを使用して
        mCalendarAdapter = new CalendarAdapter(getContext());
        calendarGridView.setAdapter(mCalendarAdapter);
        //ツールバーに現在月を表示させる処理
        //titleText.setText(mCalendarAdapter.getTitle());
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
                //選択されたセルのViewIdを取得
                TextView selectedDateText =(TextView) view.findViewById(R.id.dateText);
                //トーストメッセージ作成
                String message = selectedDateText.getText().toString() + "日が選択されました。";
                //トーストを表示
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                //ログだし
                Log.d("ClickEvent:","clicked position ->"+Integer.toString(position));
            }
        });
        return rootView;
    }

}