package com.max.mvvmsample.data.network

import com.max.mvvmsample.data.network.response.ResponseData
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    companion object{

        //TODO Set Url
        private const val baseUrl = "url"

        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ) : ApiService {

            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }

    //TODO Set Api
    @FormUrlEncoded
    @POST("post.php")
    fun post(@Field("data") data: String): Call<ResponseData>
}