package com.lxn.gdghanoicheckin.network.retrofit

import com.lxn.gdghanoicheckin.network.model.APICallResponse
import com.lxn.gdghanoicheckin.network.model.DataResponse
import com.lxn.gdghanoicheckin.network.model.SaveEmailScannedBody
import com.lxn.gdghanoicheckin.network.model.SaveQRLinkBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @author Nam Lx
 * Create on 18/07/2021
 * Contact me: namlxcntt@gmail.com
 */
const val GET_EMAIL_CREATE_QR = "GET_EMAIL_CREATE_QR"
const val GET_EMAIL_SCANNED_QR = "GET_EMAIL_SCANED_QR"
const val POST_ACTION_SAVE_EMAIL_SCANNED ="checkEmail"
const val POST_ACTION_SAVE_QR = "POST_ACTION_SAVE_QR"

interface ApiService {

    /**
     * Method send object created qr
     */
    @POST("exec")
    suspend fun sendQrCreated(@Body smsObject: SaveQRLinkBody)

    /**
     * Method send object scanned Qr
     */
    @POST("exec")
    fun sendQrScanned(@Query("action") action:String ,@Query("email") email:String ) : Call<ResponseBody>

    @POST("exec")
    fun sendQrScannedBody(@Body emailScannedBody: SaveEmailScannedBody) : Call<APICallResponse>

    @POST("exec")
    fun _sendQrScannedBody(@Body emailScannedBody: SaveEmailScannedBody) : Call<ResponseBody>

    @POST("exec")
    suspend fun sendQrScannedParam(@Query("action") action:String ,@Query("email") email:String): Call<APICallResponse>



    /**
     * Viết thế này cho nhanh
     * Còn bthg dùng @query
     */
    @GET("exec?method=$GET_EMAIL_CREATE_QR")
    suspend fun getEmailsCreateQR(): DataResponse

    @GET("exec?method=$GET_EMAIL_SCANNED_QR")
    suspend fun getEmailScanned(): List<String>


}