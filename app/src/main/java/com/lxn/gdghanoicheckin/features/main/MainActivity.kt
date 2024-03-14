package com.lxn.gdghanoicheckin.features.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lxn.gdghanoicheckin.R
import com.lxn.gdghanoicheckin.features.create.CreateQRActivity
import com.lxn.gdghanoicheckin.features.scan.ScanQRActivity
import com.lxn.gdghanoicheckin.utils.PermissionsHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel
        setContentView(R.layout.activity_main)
        requestPermissions()
        btn_checkin.setOnClickListener {
            ScanQRActivity.startActivity(this)
        }
        /*btn_create_qr.setOnClickListener {
            CreateQRActivity.startActivity(this)
        }*/
    }


    private fun requestPermissions() {
        PermissionsHelper.requestNotGrantedPermissions(
            this,
            ScanQRActivity.PERMISSIONS,
            ScanQRActivity.PERMISSION_REQUEST_CODE
        )
    }

    private fun areAllPermissionsGranted(): Boolean {
        return PermissionsHelper.areAllPermissionsGranted(this, ScanQRActivity.PERMISSIONS)
    }

    private fun areAllPermissionsGranted(grantResults: IntArray): Boolean {
        return PermissionsHelper.areAllPermissionsGranted(grantResults)
    }


}