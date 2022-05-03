@file:Suppress("unused")

package com.max.mvvmsample.view

import android.content.Context
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.max.mvvmsample.utils.ApiConnectFailException
import com.max.mvvmsample.utils.InputException
import com.max.mvvmsample.utils.NoInternetException

fun Context.toast(message: Int) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.toast(t: Throwable) {

    when (t) {
        is NoInternetException, is ApiConnectFailException, is InputException -> t.message?.let { toast(it) }
    }
}

fun View.snackbar(message: Int) = Snackbar.make(this, message, BaseTransientBottomBar.LENGTH_SHORT).apply { setAction("OK") { dismiss() } }.show()

fun View.snackbar(message: String) = Snackbar.make(this, message,
    BaseTransientBottomBar.LENGTH_SHORT
).apply { setAction("OK") { dismiss() } }.show()

fun View.snackbar(t: Throwable) {

    when (t) {
        is NoInternetException, is ApiConnectFailException, is InputException -> t.message?.let { snackbar(it) }
    }
}

fun Window.hideSoftKeyboard() = with(WindowInsetsControllerCompat(this, findViewById(android.R.id.content))) {
    hide(WindowInsetsCompat.Type.ime())
    systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
}