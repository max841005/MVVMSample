package com.max.mvvmsample.data.resource

import android.content.Context

class ResourceProvider(
    context: Context
) {

    private val appContext = context.applicationContext

    fun getString(resId: Int) = appContext.getString(resId)

    fun getString(resId: Int, formatArgs: Int) = appContext.getString(resId, formatArgs)

    fun getString(resId: Int, formatArgs: String) = appContext.getString(resId, formatArgs)
}