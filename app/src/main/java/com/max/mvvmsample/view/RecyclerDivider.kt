package com.max.mvvmsample.view

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.max.mvvmsample.R

class RecyclerDivider(
    private val context: Context,
    type: Int
) : ItemDecoration() {

    companion object {
        const val LIGHT = 0
        const val DARK = 1
    }

    private val dividerId = when (type) {
        DARK -> R.drawable.gray_dark_line
        else -> R.drawable.gray_line
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        ContextCompat.getDrawable(context, dividerId)?.let {

            val dividerLeft = parent.paddingLeft
            val dividerRight = parent.width - parent.paddingRight
            val childCount = parent.childCount

            for (i in 0..childCount - 2) {

                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val dividerTop = child.bottom + params.bottomMargin
                val dividerBottom = dividerTop + it.intrinsicHeight

                it.apply { setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom) }.draw(c)
            }
        }
    }
}