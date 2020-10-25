package com.non_name_hero.calenderview.utils.dialogUtils

import com.non_name_hero.calenderview.data.ScheduleGroup
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadDialogBase.DialogCallback

interface PigLeadDeleteDialog : PigLeadDialogBase {
    fun getDeleteDialog(scheduleGroup: ScheduleGroup?, callback: DialogCallback): PigLeadDialogFragment?
}