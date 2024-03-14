package com.lxn.gdghanoicheckin.network.model

import com.google.gson.annotations.SerializedName

data class EmailScannedResponse(

    @SerializedName("data") var data: ArrayList<String> = arrayListOf()

)