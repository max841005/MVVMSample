package com.max.mvvmsample.data.network.response

import com.google.gson.annotations.SerializedName

//TODO Set Response
data class LoginResponse (

    @SerializedName("success")
    val success: String,

    @SerializedName("message")
    val message: Message,

    @SerializedName("sid")
    val sid: String
) {

    data class Message(

        @SerializedName("ch")
        val ch: String,

        @SerializedName("en")
        val en: String
    )
}