package com.lxn.gdghanoicheckin.repository

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.lxn.gdghanoicheckin.constant.TypeCheckIn
import com.lxn.gdghanoicheckin.network.model.DataResponse
import com.lxn.gdghanoicheckin.network.model.SaveEmailScannedBody
import com.lxn.gdghanoicheckin.network.model.SaveObject
import com.lxn.gdghanoicheckin.network.model.SaveQRLinkBody

interface CheckInRepository {

    suspend fun getEmailsCreateQr() : DataResponse

    suspend fun createBitMapQr(data : String) : Bitmap?

    suspend fun pushQrToFirebase(data : Pair<String,Bitmap>, handleSuccess : (SaveQRLinkBody) -> Unit)

    fun getRandomString(length : Int = 40) : String

    suspend fun sendQRCreated(saveQRLinkBody: SaveQRLinkBody)

    suspend fun getAllEmailScanned()

    suspend fun sendQrScanned(saveEmailScannedBody: SaveEmailScannedBody,state: MutableLiveData<Pair<TypeCheckIn, String>>)

}