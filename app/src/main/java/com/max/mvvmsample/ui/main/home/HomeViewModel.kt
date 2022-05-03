@file:Suppress("unused")

package com.max.mvvmsample.ui.main.home

import androidx.lifecycle.ViewModel
import com.max.mvvmsample.data.entities.HomeEntity
import com.max.mvvmsample.data.repositories.HomeRepository
import com.max.mvvmsample.utils.toDelayTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val homeRepository: HomeRepository
) : ViewModel() {

    val entity = HomeEntity()

    private var startTime = 0L

    fun setStartTime() { startTime = System.currentTimeMillis()
    }
    fun getDelayTime() = startTime.toDelayTime()

    suspend fun getDBData() = withContext(Dispatchers.IO) { homeRepository.getDBData() }
}