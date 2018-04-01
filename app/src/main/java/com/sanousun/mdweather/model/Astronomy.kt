package com.sanousun.mdweather.model

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by dashu on 2017/6/16.
 * 天文数值数据模型
 */

data class Astronomy(
        /**
         * 月升时间（hh:mm）
         */
        val mr: String,
        /**
         * 月落时间
         */
        val ms: String,
        /**
         * 日出时间
         */
        val sr: String,
        /**
         * 日落时间
         */
        val ss: String
) {
    /**
     * 获取当前的时间占整个日出日落的比例
     */
    fun getCurrentRateForSun(): Float {
        val sdf = SimpleDateFormat("HH:mm", Locale.CHINA)
        val riseTime = sdf.parse(sr).time
        val setTime = sdf.parse(ss).time
        val nowTime = Date().time
        return (nowTime - riseTime) * 1F / (setTime - riseTime)
    }

    /**
     * 获取当前的时间占整个月出月落的比例
     */
    fun getCurrentRateForMoon(): Float {
        val sdf = SimpleDateFormat("HH:mm", Locale.CHINA)
        val riseTime = sdf.parse(mr).time
        val setTime = sdf.parse(ms).time
        val nowTime = Date().time
        return (nowTime - riseTime) * 1F / (setTime - riseTime)
    }

    /**
     * 获取当前小时数
     */
    fun getCurrentHour(): String {
        val sdf = SimpleDateFormat("HH", Locale.CHINA)
        return sdf.format(Date())
    }

    fun isDay(): Boolean {
        val sdf = SimpleDateFormat("HH:mm", Locale.CHINA)
        val riseTime = sdf.parse(sr).time
        val setTime = sdf.parse(ss).time
        val nowTime = sdf.parse(sdf.format(Date())).time
        return nowTime in (riseTime + 1)..(setTime - 1)
    }
}