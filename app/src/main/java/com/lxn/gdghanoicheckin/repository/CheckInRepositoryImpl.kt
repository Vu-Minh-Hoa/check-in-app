package com.lxn.gdghanoicheckin.repository

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.gson.Gson
import com.lxn.gdghanoicheckin.R
import com.lxn.gdghanoicheckin.constant.Constant
import com.lxn.gdghanoicheckin.constant.Constant.SIZE_QR
import com.lxn.gdghanoicheckin.constant.TypeCheckIn
import com.lxn.gdghanoicheckin.network.cache.EmailCaching
import com.lxn.gdghanoicheckin.network.model.APICallResponse
import com.lxn.gdghanoicheckin.network.model.DataResponse
import com.lxn.gdghanoicheckin.network.model.SaveEmailScannedBody
import com.lxn.gdghanoicheckin.network.model.SaveQRLinkBody
import com.lxn.gdghanoicheckin.network.retrofit.ApiService
import com.lxn.gdghanoicheckin.network.retrofit.POST_ACTION_SAVE_QR
import com.lxn.gdghanoicheckin.qr.BarcodeImageGenerator
import com.lxn.gdghanoicheckin.qr.BarcodeImageGenerator.addOverlayToCenter
import com.lxn.gdghanoicheckin.utils.logError
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class CheckInRepositoryImpl @Inject constructor(

    private val apiService: ApiService,
    private val firebaseStorage: FirebaseStorage,
    private val application: Application
) : CheckInRepository {


    override suspend fun getEmailsCreateQr(): DataResponse {
        return apiService.getEmailsCreateQR()
    }

    override suspend fun createBitMapQr(data: String): Bitmap? {
        val bitmap = BarcodeImageGenerator.scaleBitmap(
            BarcodeImageGenerator.generateBitmap(
                content = data, width = SIZE_QR, height = SIZE_QR, margin = 2
            ).copy(Bitmap.Config.ARGB_8888, true), 1344F, 1344F
        ) ?: return null
        val bitmapCenter = BitmapFactory.decodeResource(application.resources, R.drawable.bg_qr)
            .copy(Bitmap.Config.ARGB_8888, true)
        return bitmapCenter.addOverlayToCenter(bitmap)

    }

    override suspend fun pushQrToFirebase(
        data: Pair<String, Bitmap>, handleSuccess: (SaveQRLinkBody) -> Unit
    ) {
        try {
            val storageRef = firebaseStorage.reference.root.child(Constant.CHILD_NODE_FIREBASE)
                .child(getRandomString(50))
            val byteArrayOutPut = ByteArrayOutputStream()
            data.second.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutPut)
            val dataByteArray = byteArrayOutPut.toByteArray()

            val uploadTask = storageRef.putBytes(dataByteArray)

            val downloadUrlListener: OnSuccessListener<Uri>

            var valueListener: OnSuccessListener<UploadTask.TaskSnapshot>? = null

            var onFailureListener: OnFailureListener? = null

            downloadUrlListener = OnSuccessListener<Uri> {
                val saveObject = SaveQRLinkBody(
                    action = POST_ACTION_SAVE_QR, linkQr = it.toString().trim(), email = data.first
                )
                handleSuccess.invoke(saveObject)

                onFailureListener?.let { it1 ->
                    uploadTask.removeOnFailureListener(it1)
                    onFailureListener = null
                }
                valueListener?.let { it1 ->
                    uploadTask.removeOnSuccessListener(it1)
                    valueListener = null
                }

            }

            valueListener = OnSuccessListener<UploadTask.TaskSnapshot> {
                storageRef.downloadUrl.addOnSuccessListener(downloadUrlListener)
            }

            onFailureListener = OnFailureListener {
                logError("Upload image failure")
                onFailureListener = null
                valueListener = null
            }

            valueListener?.let {
                uploadTask.addOnSuccessListener(it)
            }

            onFailureListener?.let {
                uploadTask.addOnFailureListener(it)
            }
        } catch (exception: Exception) {
            logError("Upload image failure -> ${exception.message}")
        }
    }

    override fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length).map { allowedChars.random() }.joinToString("")
    }

    override suspend fun sendQRCreated(saveQRLinkBody: SaveQRLinkBody) {
        return apiService.sendQrCreated(saveQRLinkBody)
    }

    override suspend fun getAllEmailScanned() {
        try {
            val listData = apiService.getEmailScanned()
            if (listData.isNotEmpty()) {
                EmailCaching.appendListEmail(listData)
            }
        } catch (exceptions: Exception) {
            logError("getAllEmailScanned-> ${exceptions.message}")
        }
    }

    override suspend fun sendQrScanned(
        saveEmailScannedBody: SaveEmailScannedBody,
        state: MutableLiveData<Pair<TypeCheckIn, String>>
    ) {


        Log.d("GIANG_LOG", "POST CALLED")

        return apiService.sendQrScanned(
            saveEmailScannedBody.action, saveEmailScannedBody.email
        ).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>, response: Response<ResponseBody>
                ) {

                    val responseRaw = response.body()?.string()
                    Log.d("GIANG_LOG", "response body: $responseRaw")
                    if (responseRaw?.contains("<html>") == true) {
                        state.value = Pair(TypeCheckIn.ApiError, "AppScript Error!")
                        Log.d("GIANG_LOG", "AppScript Error $responseRaw")
                    } else {
                        val gson = Gson()
                        try {
                            val responseJson =
                                gson.fromJson(responseRaw, APICallResponse::class.java)
                            Log.d(
                                "GIANG_LOG",
                                "json status: ${responseJson.status}\tjson message: ${responseJson.message}"
                            )

                            when (responseJson.status) {
                                "0" -> {
                                    state.value =
                                        Pair(TypeCheckIn.NoAccount, saveEmailScannedBody.email)
                                }

                                "1" -> {
                                    state.value =
                                        Pair(TypeCheckIn.Success, saveEmailScannedBody.email)
                                }

                                else -> {
                                    state.value =
                                        Pair(TypeCheckIn.NetworkError, saveEmailScannedBody.email)
                                }
                            }
                        } catch (e: java.lang.Exception) {
                            state.value = Pair(TypeCheckIn.NetworkError, saveEmailScannedBody.email)
                            e.printStackTrace()
                        }
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("GIANG_LOG", "failure message: ${t.message}")

                    if (t.message?.contains("Unable to resolve host") == true) {
                        state.value = Pair(TypeCheckIn.NetworkError, saveEmailScannedBody.email)
                    } else {
                        state.value = Pair(TypeCheckIn.NetworkError, saveEmailScannedBody.email)
                    }
                }
            })
    }

}