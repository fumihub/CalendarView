package com.non_name_hero.calenderview.calendar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdView;
import com.non_name_hero.calenderview.R;

//フラグメントにカレンダーのビューを表示させる
public class ScreenSlidePageFragment extends Fragment {

    private TextView titleText;
    private CalendarAdapter mCalendarAdapter;
    private GridView calendarGridView;

    //onCreateViewは戻り値のビューを表示させる
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        calendarGridView = rootView.findViewById(R.id.calendarGridView);
        mCalendarAdapter = new CalendarAdapter(getContext());
        calendarGridView.setAdapter(mCalendarAdapter);
        //titleText.setText(mCalendarAdapter.getTitle());

        return rootView;
    }

}