package com.sanousun.mdweather.support.util

import android.graphics.Canvas
import android.graphics.Color
import android.support.annotation.ColorInt
import android.text.TextPaint

/**
 * Created by dashu on 2017/6/24.
 * 常用的类扩展
 */

class CommonExtension

fun Canvas.drawTextWithCenter(text: String, centerX: Float, centerY: Float, textPaint: TextPaint) {
    val fontMetrics = textPaint.fontMetrics
    // 计算文字baseline
    val textBaseY = centerY - fontMetrics.top / 2 - fontMetrics.bottom / 2
    this.drawText(text, centerX, textBaseY, textPaint)
}

fun Color.isNeedDark(@ColorInt color: Int): Boolean {
    val hsv = FloatArray(3)
    Color.colorToHSV(color, hsv)
    return Math.sqrt(Math.pow((1.0f - hsv[2]).toDouble(), 2.0) + Math.pow(hsv[1].toDouble(), 2.0)) < 0.5
}