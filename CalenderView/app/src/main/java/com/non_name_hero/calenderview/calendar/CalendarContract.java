package com.non_name_hero.calenderview.calendar;
//utill からベースを取得
import com.non_name_hero.calenderview.utils.BasePresenter;
import com.non_name_hero.calenderview.utils.BaseView;

public interface CalendarContract {

        //viewへのの出力、入力のインターフェース
        interface View extends BaseView<Presenter> {
            //TODO カレンダーへ情報を
            //TODO カレンダーセル押下時インターフェース

        }

        //Presenterへの入力、出力のインターフェース
        interface Presenter extends BasePresenter {
            //TODO カレンダー取得インターフェース

        }


}
