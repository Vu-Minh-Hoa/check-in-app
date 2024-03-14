package com.lxn.gdghanoicheckin.popup

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.DialogFragment
import com.lxn.gdghanoicheckin.R

class PopupRequirePassword(
    private val onClickApprove: () -> Unit,
) : DialogFragment() {

    companion object {
        private const val PASSWORD = "GDG@123"

        fun newInstance(
            onClickApprove: () -> Unit,
        ): PopupRequirePassword {
            return PopupRequirePassword(onClickApprove)
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = context?.let { Dialog(it, R.style.AppThemeNew_DialogTheme) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.layout_dialog_require_password)
        dialog?.show()

        val edtPassword = dialog?.findViewById<AppCompatEditText>(R.id.edt_password)

        val btnAccept = dialog?.findViewById<AppCompatButton>(R.id.btn_accept)

        btnAccept?.setOnClickListener {
            if (!edtPassword?.text.isNullOrEmpty() && edtPassword?.text.toString() == PASSWORD){
                onClickApprove.invoke()
                dialog.dismiss()
            }else{
                edtPassword?.error = "Mật khẩu sai vui lòng thử lại"
            }
        }
        return dialog!!
    }


}