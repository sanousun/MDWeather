package com.sanousun.mdweather.model

/**
 * Created by dashu on 2017/6/16.
 * 温度数据模型
 */

data class Temperature(
        /**
         * 最低温度
         */
        val min: Int,
        /**
         * 最高温度
         */
        val max: Int
) {
    fun getRange(): Int {
        return max - min
    }
}