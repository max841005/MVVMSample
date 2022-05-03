package com.max.mvvmsample.ui.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import androidx.core.view.WindowInsetsCompat.Type.ime
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import org.kodein.di.DIAware

abstract class BaseActivityFullScreen : AppCompatActivity(), DIAware, View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {

        hideNavigationBars(true)

        setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { _, insets ->

            if (!insets.isVisible(ime())) hideNavigationBars(true)

            insets
        }

        super.onCreate(savedInstanceState)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {

        hideNavigationBars(hasFocus)

        super.onWindowFocusChanged(hasFocus)
    }

    override fun onClick(view: View) {

        WindowInsetsControllerCompat(window, findViewById(android.R.id.content)).run {
            hide(ime())
            systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }


    private fun hideNavigationBars(decor: Boolean) {

        setDecorFitsSystemWindows(window, decor)

        WindowInsetsControllerCompat(window, findViewById(android.R.id.content)).run {
            hide(systemBars())
            systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}