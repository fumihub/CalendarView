package com.non_name_hero.calenderview.inputForm;


import androidx.annotation.NonNull;

import com.non_name_hero.calenderview.data.Schedule;
import com.non_name_hero.calenderview.data.source.ScheduleDataSource;
import com.non_name_hero.calenderview.data.source.ScheduleRepository;

import java.util.Date;

public class InputPresenter implements InputContract.Presenter {

    private InputContract.View mInputFormView;
    private ScheduleRepository mScheduleRepository;

    public InputPresenter(InputContract.View view ,@NonNull ScheduleRepository scheduleRepository){
        mInputFormView = view;
        mScheduleRepository = scheduleRepository;
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void saveSchedule(String title, String description, Date startAtSchedule, Date endAtSchedule, int groupId, int paymentId) {
        mScheduleRepository.setSchedule(new Schedule(title, description, startAtSchedule, endAtSchedule, groupId, paymentId), new ScheduleDataSource.SaveScheduleCallback() {
            @Override
            public void onScheduleSaved() {
                //データの書き込みが終了した時に実行
                mInputFormView.finishInput();
            }

            @Override
            public void onDataNotSaved() {
                //使用しない、、できない、、例外処理が実装されていない
            }
        });
    }
}
