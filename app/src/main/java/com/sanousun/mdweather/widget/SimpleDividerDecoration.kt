package com.sanousun.mdweather.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.sanousun.mdweather.R

/**
 * Created by dashu on 2017/6/25.
 * recyclerView分割线的简单实现
 */

class SimpleDividerDecoration(context: android.content.Context) : android.support.v7.widget.RecyclerView.ItemDecoration() {

    private var dividerHeight: Int = 0
    private var dividerPaint: android.graphics.Paint = android.graphics.Paint()
    var includeBottom = false

    override fun getItemOffsets(outRect: android.graphics.Rect, view: android.view.View, parent: android.support.v7.widget.RecyclerView, state: android.support.v7.widget.RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = dividerHeight
    }

    override fun onDraw(c: android.graphics.Canvas, parent: android.support.v7.widget.RecyclerView, state: android.support.v7.widget.RecyclerView.State) {
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
        dividerPaint.color = android.support.v4.content.ContextCompat.getColor(context, com.sanousun.mdweather.R.color.divider_color)
        dividerHeight = context.resources.getDimensionPixelSize(com.sanousun.mdweather.R.dimen.divider_height)
    }
}
