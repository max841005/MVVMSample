@file:Suppress("unused")

package com.max.mvvmsample.data.repositories

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import com.max.mvvmsample.data.config.SERVICE_CHANNEL_ID
import com.max.mvvmsample.data.network.ApiService
import com.max.mvvmsample.data.preferences.PreferenceProvider
import com.max.mvvmsample.data.resource.ResourceProvider
import com.max.mvvmsample.utils.PermissionCheckUtils

class WelcomeRepository(
    private val permission: PermissionCheckUtils,
    private val resource: ResourceProvider,
    private val preference: PreferenceProvider,
    private val api: ApiService
) {

    companion object {
        const val PERMISSION = 0
        const val BACKGROUND_LOCATION_PERMISSION = 1
    }

    val permissionArray = when {

        Build.VERSION.SDK_INT < Build.VERSION_CODES.S -> arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        else -> arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )
    }

    //TODO Set Service Channel Title
    val notificationChannelList: List<Pair<String, String>> = listOf(
        Pair(SERVICE_CHANNEL_ID, "Service Channel Title")
    )

    /**
     * @return If need to request permission, return true.
     */
    fun isNeedAskPermission() = permission.checkSelfPermission(permissionArray)

    /**
     * @param grantResults Result of requesting permission.
     * @return If need to request permission again, return true.
     */
    fun isNeedAskPermission(
        grantResults: IntArray
    ) = when {
        grantResults.isEmpty() -> true
        else -> grantResults.any { it != PackageManager.PERMISSION_GRANTED }
    }

    /**
     * If need to request background location permission, return true.
     */
    fun isNeedAskBackgroundLocationPermission() = when {
        Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> false
        else -> permission.checkSelfPermission(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
    }
}