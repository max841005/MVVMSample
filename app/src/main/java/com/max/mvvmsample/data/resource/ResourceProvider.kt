@file:Suppress("unused")

package com.max.mvvmsample.data.resource

import android.content.Context
import androidx.core.content.ContextCompat

class ResourceProvider(
    context: Context
) {

    private val appContext = context.applicationContext

    fun getString(
        resId: Int
    ) = appContext.getString(resId)

    fun getString(
        resId: Int,
        formatArgs: Int
    ) = appContext.getString(resId, formatArgs)

    fun getString(
        resId: Int,
        formatArgs: Float
    ) = appContext.getString(resId, formatArgs)

    fun getString(
        resId: Int,
        formatArgs: String
    ) = appContext.getString(resId, formatArgs)

    fun getIntArray(
        resId: Int
    ): IntArray = appContext.resources.getIntArray(resId)

    fun getStringArray(
        resId: Int
    ): Array<String> = appContext.resources.getStringArray(resId)

    fun getColor(
        resId: Int
    ) = ContextCompat.getColor(appContext, resId)

    fun getDimension(
        resId: Int
    ) = appContext.resources.getDimension(resId)

    fun getDrawable(
        resId: Int
    ) = ContextCompat.getDrawable(appContext, resId)
}