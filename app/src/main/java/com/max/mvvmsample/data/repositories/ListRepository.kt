@file:Suppress("unused")

package com.max.mvvmsample.data.repositories

import com.max.mvvmsample.data.network.ApiService
import com.max.mvvmsample.data.network.SafeApiRequest
import com.max.mvvmsample.data.preferences.PreferenceProvider
import com.max.mvvmsample.data.resource.ResourceProvider

class ListRepository(
    private val resource: ResourceProvider,
    private val api: ApiService,
    private val preference: PreferenceProvider
) : SafeApiRequest() {

    //TODO Set Connect Fail Message
    suspend fun post() = apiRequest { api.post("data") }.let { it.data }
}