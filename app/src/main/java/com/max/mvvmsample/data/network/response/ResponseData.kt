package com.max.mvvmsample.data.network.response

import com.google.gson.annotations.SerializedName

//TODO Set Response
data class ResponseData (

    @SerializedName("success")
    val success: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    var data: List<Data>
) {

    data class Data(

        @SerializedName("data")
        var data: String
    )
}