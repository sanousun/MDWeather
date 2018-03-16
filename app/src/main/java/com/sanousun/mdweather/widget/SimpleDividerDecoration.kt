package com.sanousun.mdweather.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.sanousun.mdweather.R
import com.sanousun.mdweather.R.color.divider_color

/**
 * Created by dashu on 2017/6/25.
 * recyclerView分割线的简单实现
 */

class SimpleDividerDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private var dividerHeight: Int = 0
    private var dividerPaint: Paint = Paint()
    var includeBottom = false

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = dividerHeight
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val total = if (includeBottom) childCount - 1 else childCount - 1 - 1
        for (i in 0..total) {
            val view = parent.getChildAt(i)
            val top = view.bottom
            val bottom = view.bottom + dividerHeight
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), dividerPaint)
        }
    }

    init {
        dividerPaint.color = ContextCompat.getColor(context, divider_color)
        dividerHeight = context.resources.getDimensionPixelSize(R.dimen.divider_height)
    }
}
