package com.lxn.gdghanoicheckin.network.model

import com.google.gson.annotations.SerializedName

data class SaveEmailScannedBody(
    @SerializedName("action")
    var action: String,
    @SerializedName("email")
    var email: String
)