package com.lxn.gdghanoicheckin.features.scan

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.budiyev.android.codescanner.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.lxn.gdghanoicheckin.utils.BarcodeParser
import com.lxn.gdghanoicheckin.utils.PermissionsHelper
import com.lxn.gdghanoicheckin.R
import com.lxn.gdghanoicheckin.features.confirm.ConfirmActivity
import com.lxn.gdghanoicheckin.utils.logError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_scan_qractivity.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScanQRActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner

    private val scanQrViewModel : ScanQrViewModel by viewModels()

    companion object {
        val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        const val PERMISSION_REQUEST_CODE = 101
        const val ZXING_SCAN_INTENT_ACTION = "com.google.zxing.client.android.SCAN"
        const val CONTINUOUS_SCANNING_PREVIEW_DELAY = 500L

        fun startActivity(context: Context) {
            val intent: Intent = Intent(context, ScanQRActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scanQrViewModel
        setContentView(R.layout.activity_scan_qractivity)
        initScanner()
        setTransparentStatusBar()
    }

    private fun setTransparentStatusBar() {
        if (Build.VERSION.SDK_INT in 21..29) {
            window.statusBarColor = Color.TRANSPARENT
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
        if (Build.VERSION.SDK_INT >= 30) {
            // Root ViewGroup of my activity
            val root = findViewById<ConstraintLayout>(R.id.root)

            ViewCompat.setOnApplyWindowInsetsListener(root) { view, windowInsets ->

                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

                view.layoutParams =  (view.layoutParams as FrameLayout.LayoutParams).apply {
                    leftMargin = insets.left
                    bottomMargin = insets.bottom
                    rightMargin = insets.right
                }

                WindowInsetsCompat.CONSUMED
            }
        }
    }


    private fun handleScannedBarcode(result: Result) {
        val barcode = BarcodeParser.parseResult(result)
        lifecycleScope.launch {
            delay(500L)
            ConfirmActivity.startActivity(this@ScanQRActivity,barcode)
        }
    }

    private fun showError(error: Throwable?) {
        logError(error?.message.toString())
    }

    override fun onResume() {
        super.onResume()
        if (areAllPermissionsGranted()) {
            codeScanner.startPreview()
        }
        //scanQrViewModel.getListEmailScanned()
    }

    override fun onPause() {
        super.onPause()
        codeScanner.releaseResources()
    }

    private fun areAllPermissionsGranted(): Boolean {
        return PermissionsHelper.areAllPermissionsGranted(this, PERMISSIONS)
    }


    private fun initScanner() {
        codeScanner = CodeScanner(this, scanner_view).apply {
            camera = CodeScanner.CAMERA_BACK
            autoFocusMode = AutoFocusMode.CONTINUOUS
            formats = listOf(BarcodeFormat.QR_CODE)
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isTouchFocusEnabled = false
            decodeCallback = DecodeCallback(::handleScannedBarcode)
            errorCallback = ErrorCallback(::showError)
        }
    }

}