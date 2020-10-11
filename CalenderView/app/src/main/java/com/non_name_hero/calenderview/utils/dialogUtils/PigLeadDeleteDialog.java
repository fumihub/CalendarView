package com.non_name_hero.calenderview.utils.dialogUtils;

import androidx.annotation.NonNull;

import com.non_name_hero.calenderview.data.ScheduleGroup;

public interface PigLeadDeleteDialog extends PigLeadDialogBase {
    PigLeadDialogFragment getDeleteDialog(ScheduleGroup scheduleGroup, @NonNull DialogCallback callback);
}
