@file:Suppress("unused")

package com.max.mvvmsample.data.repositories

import com.max.mvvmsample.data.db.AppDatabase
import com.max.mvvmsample.data.db.entities.DBData
import com.max.mvvmsample.data.network.ApiService
import com.max.mvvmsample.data.preferences.PreferenceProvider
import com.max.mvvmsample.data.resource.ResourceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepository(
    private val resource: ResourceProvider,
    private val api: ApiService,
    private val db: AppDatabase,
    private val preference: PreferenceProvider
) {

    suspend fun insertDBData(
        dbData: DBData
    ) = withContext(Dispatchers.IO) { db.getDBDataDao().insert(dbData) }

    suspend fun getDBData() = withContext(Dispatchers.IO) { db.getDBDataDao().getData(1) }
}