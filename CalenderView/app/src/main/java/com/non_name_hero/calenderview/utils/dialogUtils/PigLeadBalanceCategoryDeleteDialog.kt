package com.non_name_hero.calenderview.utils.dialogUtils

import com.non_name_hero.calenderview.data.CategoryData
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadDialogBase.DialogCallback

interface PigLeadBalanceCategoryDeleteDialog : PigLeadDialogBase {
    fun getBalanceCategoryDeleteDialog(categoryData: CategoryData?, callback: DialogCallback): PigLeadDialogFragment?
}