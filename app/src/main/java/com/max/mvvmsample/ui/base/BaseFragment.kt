package com.max.mvvmsample.ui.base

import android.view.View
import androidx.core.view.WindowInsetsCompat.Type.ime
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import androidx.fragment.app.Fragment
import org.kodein.di.DIAware

abstract class BaseFragment : Fragment(), DIAware, View.OnClickListener {

    override fun onClick(view: View) {

        with(requireActivity()) {

            WindowInsetsControllerCompat(window, findViewById(android.R.id.content)).run {
                hide(ime())
                systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
}