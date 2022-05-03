@file:Suppress("unused")

package com.max.mvvmsample.utils

import android.content.Context
import android.content.Intent

class BroadcastUtils(
    context: Context
) {

    private val appContext = context.applicationContext

    fun broadcast(action: String) = appContext.sendBroadcast(Intent(action))
}