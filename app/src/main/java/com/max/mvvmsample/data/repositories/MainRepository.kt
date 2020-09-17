package com.max.mvvmsample.data.repositories

import com.max.mvvmsample.data.network.ApiService
import com.max.mvvmsample.data.preferences.PreferenceProvider
import com.max.mvvmsample.data.resource.ResourceProvider

class MainRepository(
    private val resource: ResourceProvider,
    private val api: ApiService,
    private val preference: PreferenceProvider
) {

}