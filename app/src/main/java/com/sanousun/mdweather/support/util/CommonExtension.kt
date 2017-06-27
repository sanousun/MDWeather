package com.sanousun.mdweather.support.util

import android.graphics.Canvas
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
