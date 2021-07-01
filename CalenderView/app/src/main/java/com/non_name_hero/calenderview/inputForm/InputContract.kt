package com.non_name_hero.calenderview.inputForm

import com.non_name_hero.calenderview.utils.BasePresenter
import com.non_name_hero.calenderview.utils.BaseView
import java.util.*

interface InputContract {
    //viewへのの出力、入力のインターフェース
    interface View : BaseView<Presenter?> {
        fun finishInput()
    }

    //Presenterへの入力、出力のインターフェース
    interface Presenter : BasePresenter {
        fun saveSchedule(title: String?, description: String?, startAtSchedule: Date?, endAtSchedule: Date?, groupId: Int, paymentId: Int)
        fun saveBalance(price: Long, balanceCategoryId: Int, usedAtDatetime: Date, title: String?)
    }
}