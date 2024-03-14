package com.lxn.gdghanoicheckin.network.model

import com.google.gson.annotations.SerializedName

data class SaveQRLinkBody(
    @SerializedName("action")
    val action: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("linkQR")
    val linkQr: String
)