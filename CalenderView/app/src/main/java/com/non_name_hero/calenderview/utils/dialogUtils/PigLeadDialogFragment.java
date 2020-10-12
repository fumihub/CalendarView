package com.non_name_hero.calenderview.utils.dialogUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.non_name_hero.calenderview.R;

import java.util.ArrayList;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class PigLeadDialogFragment extends DialogFragment {

    private Context appContext;
    private String dialogMessage;
    private String positiveBtnMessage;
    private String negativeBtnMessage;
    private DialogInterface.OnClickListener positiveClickListener;
    private DialogInterface.OnClickListener negativeClickListener;

    public PigLeadDialogFragment(Context context) {
        this.appContext = context;
        this.dialogMessage = context.getString(R.string.dialog_massage_default);
        this.positiveBtnMessage = context.getString(R.string.dialog_positive_default);
        this.negativeBtnMessage = context.getString(R.string.dialog_negative_default);
    }

    public PigLeadDialogFragment(Context context,
                                 String title,
                                 String positiveMessage,
                                 DialogInterface.OnClickListener positiveListener,
                                 String negativeMassage,
                                 DialogInterface.OnClickListener negativeListener) {
        this.appContext = context;
        this.dialogMessage = title;
        this.positiveBtnMessage = positiveMessage;
        this.negativeBtnMessage = negativeMassage;
        this.positiveClickListener = positiveListener;
        this.negativeClickListener = negativeListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
        builder.setMessage(this.dialogMessage)
                .setPositiveButton(this.positiveBtnMessage, this.positiveClickListener)
                .setNegativeButton(this.negativeBtnMessage, this.negativeClickListener);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }

    public PigLeadDialogFragment setDialogMessage(String message) {
        this.dialogMessage = message;
        return this;
    }

    /**
     * メッセージ配列を結合して設定
     * @param messageList
     * @return
     */
    public PigLeadDialogFragment setDialogMessage(ArrayList<String> messageList) {
        StringBuilder buffer = new StringBuilder();
        boolean firstTimeFrag = TRUE;
        for (String message : messageList) {
            if (firstTimeFrag) {
                firstTimeFrag = FALSE;
            } else {
                buffer.append("\n");
            }
            buffer.append(message);
        }
        this.dialogMessage = buffer.toString();
        return this;
    }

    public PigLeadDialogFragment setPositiveBtnMessage(String positiveBtnMessage) {
        this.positiveBtnMessage = positiveBtnMessage;
        return this;
    }

    public PigLeadDialogFragment setNegativeBtnMessage(String negativeBtnMessage) {
        this.negativeBtnMessage = negativeBtnMessage;
        return this;
    }

    public PigLeadDialogFragment setPositiveClickListener(DialogInterface.OnClickListener positiveClickListener) {
        this.positiveClickListener = positiveClickListener;
        return this;
    }

    public PigLeadDialogFragment setNegativeClickListener(DialogInterface.OnClickListener negativeClickListener) {
        this.negativeClickListener = negativeClickListener;
        return this;
    }
}
