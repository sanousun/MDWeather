package com.sanousun.mdweather.support.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by dashu on 2017/6/25.
 * 关于日期操作的扩展，主要是Date，String，Calendar之间的转换
 */

class DateExtension

fun String.changeFormat(formatIn: String, formatOut: String): String {
    val sdf0 = SimpleDateFormat(formatIn, Locale.getDefault())
    val date = sdf0.parse(this)
    val sdf = SimpleDateFormat(formatOut, Locale.getDefault())
    return sdf.format(date)
}

fun String.getDayOfWeekString(format: String): String {
    val sdf0 = SimpleDateFormat(format, Locale.getDefault())
    val date = sdf0.parse(this)
    val calendar = Calendar.getInstance()
    calendar.time = date
    when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> return "周一"
        Calendar.TUESDAY -> return "周二"
        Calendar.WEDNESDAY -> return "周三"
        Calendar.THURSDAY -> return "周四"
        Calendar.FRIDAY -> return "周五"
        Calendar.SATURDAY -> return "周六"
        else -> return "周日"
    }
}