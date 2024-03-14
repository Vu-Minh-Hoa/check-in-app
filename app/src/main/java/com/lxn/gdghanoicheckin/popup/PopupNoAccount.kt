package com.lxn.gdghanoicheckin.popup

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import com.lxn.gdghanoicheckin.R

class PopupNoAccount(
    private val onClickApprove: () -> Unit,
    private val message: String = ""
) : DialogFragment() {

    companion object {
        private const val NAME_KEY = "NAME_KEY"

        fun newInstance(
            message: String,
            onClickApprove: () -> Unit,
        ): PopupNoAccount {
            return PopupNoAccount(onClickApprove,message)
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = context?.let { Dialog(it, R.style.AppThemeNew_DialogTheme) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_no_account)
        dialog?.show()


        val button = dialog?.findViewById<AppCompatButton>(R.id.btn_accept)
        val tvType = dialog?.findViewById<AppCompatTextView>(R.id.tv_type)

        button?.setOnClickListener {
            onClickApprove.invoke()
        }

        return dialog!!
    }


}