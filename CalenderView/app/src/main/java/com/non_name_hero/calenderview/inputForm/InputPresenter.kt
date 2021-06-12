package com.non_name_hero.calenderview.inputForm

import com.non_name_hero.calenderview.data.Schedule
import com.non_name_hero.calenderview.data.source.ScheduleDataSource.SaveScheduleCallback
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.inputForm.InputContract.Presenter
import java.util.*

class InputPresenter(private val inputFormView: InputContract.View, private val scheduleRepository: ScheduleRepository) : Presenter {
    override fun start() {}
    override fun saveSchedule(title: String?, description: String?, startAtSchedule: Date?, endAtSchedule: Date?, groupId: Int, paymentId: Int) {
        scheduleRepository.setSchedule(Schedule(title!!, description!!, startAtSchedule!!, endAtSchedule, groupId, paymentId), object : SaveScheduleCallback {
            override fun onScheduleSaved() {
                //データの書き込みが終了した時に実行
                inputFormView.finishInput()
            }

            override fun onDataNotSaved() {
                //使用しない、、できない、、例外処理が実装されていない
            }
        })
    }

    init {
        inputFormView.setPresenter(this)
    }
}