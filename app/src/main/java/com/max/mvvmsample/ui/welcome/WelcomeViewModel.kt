package com.max.mvvmsample.ui.welcome

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import com.max.mvvmsample.data.repositories.WelcomeRepository
import com.max.mvvmsample.utils.toDelayTime

class WelcomeViewModel(
    private val welcomeRepository: WelcomeRepository
) : ViewModel() {

    private var startTime = 0L

    fun setStartTime() { startTime = System.currentTimeMillis() }
    fun getDelayTime() = startTime.toDelayTime()

    //Permission
    val permissionArray = welcomeRepository.permissionArray

    fun isNeedAskPermission() = welcomeRepository.isNeedAskPermission()

    fun isNeedAskPermission(
        grantResults: IntArray
    ) = welcomeRepository.isNeedAskPermission(grantResults)

    fun isNeedAskBackgroundLocationPermission() = welcomeRepository.isNeedAskBackgroundLocationPermission()

    //NotificationChannel
    val cacheNotificationChannelIdList = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNotificationChannelList() = welcomeRepository.notificationChannelList.map { it.toNotificationChannel() }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun Pair<String, String>.toNotificationChannel() = NotificationChannel(this.first, this.second, NotificationManager.IMPORTANCE_HIGH).apply {
        enableLights(true)
        enableVibration(true)
        lockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE
        setShowBadge(true)
    }
}
