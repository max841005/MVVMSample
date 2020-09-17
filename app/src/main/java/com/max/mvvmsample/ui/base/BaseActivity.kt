package com.max.mvvmsample.ui.base

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.max.mvvmsample.utils.KeyboardStatus
import org.kodein.di.KodeinAware

abstract class BaseActivity : AppCompatActivity(), KodeinAware, View.OnClickListener {

    companion object {
        const val VIEW_FLAG = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val decorView = window.decorView

        decorView.systemUiVisibility = VIEW_FLAG

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            val rootView = findViewById<View>(android.R.id.content)

            rootView?.viewTreeObserver?.addOnGlobalLayoutListener {

                //Show NavigationBar when SoftKeyboard is showing.
                decorView.systemUiVisibility = when {
                    KeyboardStatus(rootView).isKeyboardShown() -> 0
                    else -> VIEW_FLAG
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (hasFocus) {
            window.decorView.systemUiVisibility = VIEW_FLAG
        }
    }
}