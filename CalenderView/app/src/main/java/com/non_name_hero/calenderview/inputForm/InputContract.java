package com.non_name_hero.calenderview.inputForm;

import androidx.annotation.Nullable;

import com.non_name_hero.calenderview.calendar.CalendarContract;
import com.non_name_hero.calenderview.utils.BasePresenter;
import com.non_name_hero.calenderview.utils.BaseView;

import java.util.Calendar;

public interface InputContract {
    //viewへのの出力、入力のインターフェース
    interface View extends BaseView<InputContract.Presenter> {

    }

    //Presenterへの入力、出力のインターフェース
    interface Presenter extends BasePresenter {
        void saveSchedule(String title, String description, Calendar startAtSchedule, Calendar endAtSchedule, int groupId, int paymentId);
    }

}
