package com.max.mvvmsample.view

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.max.mvvmsample.R
import com.max.mvvmsample.databinding.LayoutProgressBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

fun Context.toast(message: Int) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun View.snackbar(message: Int) = Snackbar.make(this, message, BaseTransientBottomBar.LENGTH_LONG).apply { setAction("OK") { dismiss() } }.show()

fun View.snackbar(message: String) = Snackbar.make(this, message, BaseTransientBottomBar.LENGTH_LONG).apply { setAction("OK") { dismiss() } }.show()

fun Context.hideSoftKeyboard(rootView: View) = (this.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(rootView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

/**
 * @param rootView View for activity root
 * @param view View for popupWindow
 */
fun AppCompatActivity.showPopupWindow(popupWindow: PopupWindow, rootView: View, view: View) {

    popupWindow.apply {

        //Set popupWindow
        animationStyle = R.style.AlertDialogAnimation
        isClippingEnabled = false
    }.let {

        //Show popupWindow if it isn't showing.
        if (!it.isShowing && !isFinishing) {
            rootView.post { it.showAtLocation(view, Gravity.CENTER, 0, 0) }
        }
    }
}

fun Context.progressDialog(message: Int) : AlertDialog {

    val view = View.inflate(this, R.layout.layout_progress, null).apply { systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY }

    val progressBinding : LayoutProgressBinding = DataBindingUtil.bind(view)!!
    progressBinding.content.setText(message)

    return MaterialAlertDialogBuilder(this).apply {
        setView(view)
        setCancelable(false)
    }.create()
}