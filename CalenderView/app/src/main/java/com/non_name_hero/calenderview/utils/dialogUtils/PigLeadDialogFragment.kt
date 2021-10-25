package com.non_name_hero.calenderview.utils.dialogUtils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Spanned
import androidx.fragment.app.DialogFragment
import com.non_name_hero.calenderview.R
import java.lang.Boolean
import java.util.*

class PigLeadDialogFragment : DialogFragment {
    private var appContext: Context
    private lateinit var dialogMessage: Spanned
    private var positiveBtnMessage: String
    private var negativeBtnMessage: String
    private var positiveClickListener: DialogInterface.OnClickListener? = null
    private var negativeClickListener: DialogInterface.OnClickListener? = null

    constructor(context: Context) {
        appContext = context
        positiveBtnMessage = context.getString(R.string.dialog_positive_default)
        negativeBtnMessage = context.getString(R.string.dialog_negative_default)
    }

    constructor(context: Context,
                title: Spanned,
                positiveMessage: String,
                positiveListener: DialogInterface.OnClickListener?,
                negativeMassage: String,
                negativeListener: DialogInterface.OnClickListener?) {
        appContext = context
        dialogMessage = title
        positiveBtnMessage = positiveMessage
        negativeBtnMessage = negativeMassage
        positiveClickListener = positiveListener
        negativeClickListener = negativeListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(appContext)
        builder.setMessage(dialogMessage)
                .setPositiveButton(positiveBtnMessage, positiveClickListener)
                .setNegativeButton(negativeBtnMessage, negativeClickListener)
        // Create the AlertDialog object and return it
        return builder.create()
    }

    fun setAppContext(appContext: Context) {
        this.appContext = appContext
    }

    fun setDialogMessage(message: Spanned): PigLeadDialogFragment {
        dialogMessage = message
        return this
    }

    fun setPositiveBtnMessage(positiveBtnMessage: String): PigLeadDialogFragment {
        this.positiveBtnMessage = positiveBtnMessage
        return this
    }

    fun setNegativeBtnMessage(negativeBtnMessage: String): PigLeadDialogFragment {
        this.negativeBtnMessage = negativeBtnMessage
        return this
    }

    fun setPositiveClickListener(positiveClickListener: DialogInterface.OnClickListener?): PigLeadDialogFragment {
        this.positiveClickListener = positiveClickListener
        return this
    }

    fun setNegativeClickListener(negativeClickListener: DialogInterface.OnClickListener?): PigLeadDialogFragment {
        this.negativeClickListener = negativeClickListener
        return this
    }
}
