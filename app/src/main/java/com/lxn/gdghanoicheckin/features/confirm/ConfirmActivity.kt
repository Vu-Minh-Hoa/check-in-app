package com.lxn.gdghanoicheckin.features.confirm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.lxn.gdghanoicheckin.R
import com.lxn.gdghanoicheckin.constant.TypeCheckIn
import com.lxn.gdghanoicheckin.features.main.MainActivity
import com.lxn.gdghanoicheckin.popup.ConfirmDialogCheckInDialogFragment
import com.lxn.gdghanoicheckin.popup.PopupNetworkError
import com.lxn.gdghanoicheckin.popup.PopupNoAccount
import com.lxn.gdghanoicheckin.popup.PopupNotEmail
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_confirm.*


@AndroidEntryPoint
class ConfirmActivity : AppCompatActivity() {

    private val viewModel: ConfirmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)

        visibilityLoading(true)
        observeData()

    }

    private fun observeData() {
        viewModel.stateConfirm.observe(this) {
            if (it == null) return@observe
            visibilityLoading(false)
            when (it.first) {
                TypeCheckIn.Existed -> {

                }
                TypeCheckIn.Success -> {
                    showConfirmPopUpDialog(it.second)
                }

                TypeCheckIn.NotEmail -> {
                    Log.d("GIANG_LOG","Not email!")
                    showPopupNotEmail(message = it.second)
                }

                TypeCheckIn.NoAccount -> {
                    showNoAccountPopUpDialog(it.second)
                }
                TypeCheckIn.NetworkError -> {
                    showErrorPopUpDialog("Lỗi! Vui lòng kiểm tra kết nối mạng!")
                }

                TypeCheckIn.ApiError -> {
                    showErrorPopUpDialog("Appscript thông báo lỗi!")
                }
            }
        }
    }

    private fun visibilityLoading(isVisible: Boolean) {
        loadingView.isVisible = isVisible
    }

    private fun showPopupNotEmail(message: String) {
        val errorDialog = PopupNotEmail.newInstance( message = message, ::clearBackStackAndGotoMain)
        errorDialog.isCancelable = false
        errorDialog.show(supportFragmentManager, "")
    }

    private fun showPopupError() {
        val errorDialog = PopupNotEmail.newInstance( message = "", ::clearBackStackAndGotoMain)
        errorDialog.isCancelable = false
        errorDialog.show(supportFragmentManager, "")
    }

    private fun showConfirmPopUpDialog(name: String) {
        val errorDialog =
            ConfirmDialogCheckInDialogFragment.newInstance(
                name = name,
                ::clearBackStackAndGotoMain,
            )
        errorDialog.isCancelable = false
        errorDialog.show(supportFragmentManager, "")
    }

    private fun showNoAccountPopUpDialog(name: String) {
        val errorDialog =
            PopupNoAccount.newInstance(
                message = name,
                ::clearBackStackAndGotoMain,
            )
        errorDialog.isCancelable = false
        errorDialog.show(supportFragmentManager, "")
    }

    private fun showErrorPopUpDialog(message: String) {
        val errorDialog =
            PopupNetworkError.newInstance(
                message = message,
                ::clearBackStackAndGotoMain,
            )
        errorDialog.isCancelable = false
        errorDialog.show(supportFragmentManager, "")
    }

    private fun clearBackStackAndGotoMain(){
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }


    companion object {
        const val KEY = "KEY_TEXT"
        fun startActivity(context: Context, data: String) {
            val intent = Intent(context, ConfirmActivity::class.java)
            intent.putExtra(KEY, data)
            context.startActivity(intent)
        }
    }



}