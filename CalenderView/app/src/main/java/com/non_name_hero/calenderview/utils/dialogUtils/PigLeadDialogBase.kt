package com.non_name_hero.calenderview.utils.dialogUtils

interface PigLeadDialogBase {
    interface DialogCallback {
        fun onClickPositiveBtn()
        fun onClickNegativeBtn()
    }

    fun showPigLeadDiaLog(dialog: PigLeadDialogFragment?)
}