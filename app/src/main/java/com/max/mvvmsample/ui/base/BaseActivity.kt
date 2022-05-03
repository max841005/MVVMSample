package com.max.mvvmsample.ui.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import org.kodein.di.DIAware

abstract class BaseActivity : AppCompatActivity(), DIAware, View.OnClickListener {

    override fun finish() {

        WindowInsetsControllerCompat(window, findViewById(android.R.id.content)).hide(WindowInsetsCompat.Type.ime())

        super.finish()
    }

    override fun onClick(view: View) {

        WindowInsetsControllerCompat(window, findViewById(android.R.id.content)).hide(WindowInsetsCompat.Type.ime())
    }
}