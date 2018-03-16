package com.sanousun.mdweather.support.util

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import android.widget.Toast

/**
 * Created by dashu on 2017/6/22.
 * Context的扩展
 */

class ContextExtension

fun Context.getScreenWidth(): Int {
    val wm: WindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val point: Point = Point()
    wm.defaultDisplay.getSize(point)
    return point.x
}

fun Context.dpToPx(dpValue: Int): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun Context.spToPx(spValue: Int): Int {
    val scale = resources.displayMetrics.scaledDensity
    return (spValue * scale + 0.5f).toInt()
}

fun Context.toastShort(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.toastLong(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}