package com.non_name_hero.calenderview.utils.dialogUtils;

public interface PigLeadDialog {
    interface DeleteDialog extends PigLeadDialogBase{
        PigLeadDialogFragment getDeleteDialog(final int position);
    }
}
