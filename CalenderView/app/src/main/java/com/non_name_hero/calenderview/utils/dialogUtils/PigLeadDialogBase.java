package com.non_name_hero.calenderview.utils.dialogUtils;

public interface PigLeadDialogBase {
    interface DialogCallback {
        void onClickPositiveBtn();
        void onClickNegativeBtn();
    }
    void showPigLeadDiaLog(PigLeadDialogFragment dialog);
}
