package com.max.mvvmsample.utils

import android.graphics.Rect
import android.view.View

/**
 * @param view RootView of layout.
 */
class KeyboardStatus(
    private val view: View
) {

    /**
     * Check SoftKeyboard showing status.
     *
     * @return If SoftKeyboard is showing, return true.
     */
    fun isKeyboardShown(): Boolean {

        val softKeyboardHeight = 100

        val rect = Rect()
        view.getWindowVisibleDisplayFrame(rect)

        val displayMetrics = view.resources.displayMetrics
        val heightDiff = view.bottom - rect.bottom

        return heightDiff > softKeyboardHeight * displayMetrics.density
    }
}