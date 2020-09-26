package com.max.mvvmsample.view

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.max.mvvmsample.R

class RecyclerDivider(
    private val context: Context,
    private val type: Type
) : ItemDecoration() {

    enum class Type(
        val dividerId: Int
    ) {
        LIGHT(R.drawable.line_gray),
        DARK(R.drawable.line_gray_dark)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        ContextCompat.getDrawable(context, type.dividerId)?.let {

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