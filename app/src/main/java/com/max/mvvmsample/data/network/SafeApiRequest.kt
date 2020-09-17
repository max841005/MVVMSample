package com.max.mvvmsample.data.network

import com.google.gson.Gson
import com.max.mvvmsample.data.resource.ResourceProvider
import com.max.mvvmsample.utils.ApiConnectFailException
import org.json.JSONObject
import retrofit2.Call

abstract class SafeApiRequest(
    resource: ResourceProvider
) {

    suspend fun<T: Any> apiRequest(call: suspend () -> Call<T>) : T = call.invoke().execute().run {

        if (!isSuccessful) throw ApiConnectFailException("Api Connect Fail")

        if (body() == null) throw ApiConnectFailException("Api Connect Fail")

        //TODO Check Success
        val json = JSONObject(Gson().toJson(body()))

        if ("0" == json.getString("success")) throw ApiConnectFailException(json.getString("message"))

        body()!!
    }

}