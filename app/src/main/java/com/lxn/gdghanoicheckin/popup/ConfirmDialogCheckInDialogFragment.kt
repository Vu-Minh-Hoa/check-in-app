package com.lxn.gdghanoicheckin.popup

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import com.lxn.gdghanoicheckin.R
import com.lxn.gdghanoicheckin.constant.TypeCheckIn

class ConfirmDialogCheckInDialogFragment(
    private val onClickApprove: () -> Unit,
) : DialogFragment() {

    companion object {
        internal const val NAME_KEY = "NAME_KEY"

        fun newInstance(
            name: String?,
            onClickApprove: () -> Unit,
        ): ConfirmDialogCheckInDialogFragment {
            return ConfirmDialogCheckInDialogFragment(onClickApprove).apply {
                arguments = Bundle().apply {
                    putString(NAME_KEY, name)
                }
            }
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val name = arguments?.getString(NAME_KEY).orEmpty()

        val dialog = context?.let { Dialog(it, R.style.AppThemeNew_DialogTheme) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_confirm_account_checkin)
        dialog?.show()

        val content = dialog?.findViewById<AppCompatTextView>(R.id.tv_content)

        content?.text = name

        val button = dialog?.findViewById<AppCompatButton>(R.id.btn_accept)

        button?.setOnClickListener {
            onClickApprove.invoke()
        }

        return dialog!!
    }


}