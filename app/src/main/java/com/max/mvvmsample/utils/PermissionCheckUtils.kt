package com.max.mvvmsample.utils

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.content.ContextCompat.checkSelfPermission

class PermissionCheckUtils(
    context: Context
) {

    private val appContext = context.applicationContext

    fun checkSelfPermission(permissionArray: Array<String>) = permissionArray.any { checkSelfPermission(appContext, it) != PERMISSION_GRANTED }
}