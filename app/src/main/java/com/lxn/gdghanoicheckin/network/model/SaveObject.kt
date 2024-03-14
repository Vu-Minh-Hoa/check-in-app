package com.lxn.gdghanoicheckin.network.model


import com.google.gson.annotations.SerializedName

data class SaveObject(
    @SerializedName("action")
    var action: String,
    @SerializedName("content")
    var content: String,
    @SerializedName("from")
    var from: String
)