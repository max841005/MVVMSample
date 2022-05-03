@file:Suppress("unused")

package com.max.mvvmsample.view

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.max.mvvmsample.R

class RecyclerDivider(
    private val context: Context,
    private val color: Color
) : ItemDecoration() {

    private val appContext = context.applicationContext

    enum class Color(
        val dividerId: Int
    ) {
        GRAY(R.drawable.line_gray)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        ContextCompat.getDrawable(appContext, color.dividerId)?.let { drawable ->

            val dividerLeft = parent.paddingLeft
            val dividerRight = parent.width - parent.paddingRight
            val childCount = parent.childCount

            (0..childCount - 2).forEach {

                val child = parent.getChildAt(it)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val dividerTop = child.bottom + params.bottomMargin
                val dividerBottom = dividerTop + drawable.intrinsicHeight

                drawable.apply { setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom) }.draw(c)
            }
        }
    }
}