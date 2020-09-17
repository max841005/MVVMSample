package com.max.mvvmsample.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class PreferenceProvider(
    context: Context
) {

    private val appContext = context.applicationContext

    private val sp: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun set(key: String, value: Int) {
        sp.edit().putInt(key, value).apply()
    }

    fun set(key: String, value: String) {
        sp.edit().putString(key, value).apply()
    }

    fun set(key: String, value: Boolean) {
        sp.edit().putBoolean(key, value).apply()
    }

    fun getInt(key: String): Int {
        return sp.getInt(key, -1)
    }

    fun getString(key: String): String {
        return sp.getString(key, "")!!
    }

    fun getBoolean(key: String): Boolean {
        return sp.getBoolean(key, false)
    }

    fun clear() : Boolean {

        sp.edit().clear().apply()

        return true
    }
}