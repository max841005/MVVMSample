package com.max.mvvmsample.data.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionCheckProvider(
    context: Context
) {

    private val appContext = context.applicationContext

    fun checkSelfPermission(permissionArray: Array<String>) = permissionArray.any { ContextCompat.checkSelfPermission(appContext, it) != PackageManager.PERMISSION_GRANTED }
}