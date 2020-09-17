package com.max.mvvmsample.ui.main.home

import androidx.lifecycle.ViewModel
import com.max.mvvmsample.data.entities.HomeEntity
import com.max.mvvmsample.data.repositories.HomeRepository
import com.max.mvvmsample.data.repositories.TimeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val timeRepository: TimeRepository,
    private val homeRepository: HomeRepository
) : ViewModel() {

    val entity = HomeEntity()
    var startTime = 0L

    //Start time
    fun setStartTime() { startTime = System.currentTimeMillis() }
    fun getDelayTime() = timeRepository.getDelayTime(startTime)

    suspend fun getDBData() = withContext(Dispatchers.IO) { homeRepository.getDBData() }
}