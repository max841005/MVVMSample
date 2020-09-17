package com.max.mvvmsample.data.repositories

import com.max.mvvmsample.data.network.ApiService
import com.max.mvvmsample.data.network.SafeApiRequest
import com.max.mvvmsample.data.preferences.PreferenceProvider
import com.max.mvvmsample.data.resource.ResourceProvider
import com.max.mvvmsample.data.time.TimeProvider

class ListRepository(
    private val resource: ResourceProvider,
    private val api: ApiService,
    private val preference: PreferenceProvider,
    private val time: TimeProvider
) : SafeApiRequest(resource) {

    //TODO Set Connect Fail Message
    suspend fun post() = apiRequest { api.post("data") }.let { it.data }
}