package com.lxn.gdghanoicheckin.network.model

import com.google.gson.annotations.SerializedName

data class APICallResponse(@SerializedName("status") var status: String, @SerializedName("message") var message: String)
