package com.max.mvvmsample.ui.welcome

import android.os.Handler
import androidx.lifecycle.ViewModel
import com.max.mvvmsample.data.repositories.TimeRepository
import com.max.mvvmsample.data.repositories.WelcomeRepository

class WelcomeViewModel(
    private val timeRepository: TimeRepository,
    private val welcomeRepository: WelcomeRepository
) : ViewModel() {

    private var startTime = 0L
    val sendRequestCode = welcomeRepository.sendRequestCode

    val handler = Handler()

    //Start time
    fun setStartTime() { startTime = System.currentTimeMillis() }
    fun getDelayTime() = timeRepository.getDelayTime(startTime)

    //Permission
    val permissionArray = welcomeRepository.permissionArray
    val isNeedAskPermission = welcomeRepository.isNeedAskPermission()
    fun isNeedAskPermission(
        receiveRequestCode: Int,
        grantResults: IntArray
    ) = welcomeRepository.isNeedAskPermission(receiveRequestCode, grantResults)
}
