package com.non_name_hero.calenderview.utils.dialogUtils

import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadDialogBase.DialogCallback

interface PigLeadRepeatCheckDialog : PigLeadDialogBase {
    fun getRepeatDialog(price: String, balanceCategoryName: String, mUsedAtDatetime: String, title: String, callback: DialogCallback): PigLeadDialogFragment?
}