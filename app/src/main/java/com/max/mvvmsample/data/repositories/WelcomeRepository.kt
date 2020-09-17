package com.max.mvvmsample.data.repositories

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import com.max.mvvmsample.data.network.ApiService
import com.max.mvvmsample.data.permission.PermissionCheckProvider
import com.max.mvvmsample.data.preferences.PreferenceProvider
import com.max.mvvmsample.data.resource.ResourceProvider
import com.max.mvvmsample.data.time.TimeProvider

class WelcomeRepository(
    private val permission: PermissionCheckProvider,
    private val resource: ResourceProvider,
    private val preference: PreferenceProvider,
    private val api: ApiService,
    private val time: TimeProvider
) {

    val sendRequestCode = 5978

    //TODO Set Need Permission
    /**
     * If the version exceeds Android 10, need ACCESS_BACKGROUND_LOCATION permission.
     */
    val permissionArray = when {

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )

        else -> arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    /**
     * @return If need to request permission, return true.
     */
    fun isNeedAskPermission() = permission.checkSelfPermission(permissionArray)

    /**
     * @param receiveRequestCode RequestCode when receiving the request permission result.
     * @param grantResults Result of requesting permission.
     *
     * @return If need to request permission again, return true.
     */
    fun isNeedAskPermission(
        receiveRequestCode: Int,
        grantResults: IntArray
    ) = when (sendRequestCode) {

        receiveRequestCode -> when {

            grantResults.isEmpty() -> true

            else -> grantResults.any { it != PackageManager.PERMISSION_GRANTED }
        }

        else -> true
    }
}