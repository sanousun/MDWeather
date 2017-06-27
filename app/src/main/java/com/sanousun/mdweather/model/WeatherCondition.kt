package com.sanousun.mdweather.model

/**
 * Created by dashu on 2017/6/16.
 * 天气状况数据模型
 */

data class WeatherCondition(
        /**
         * 白天天气状况代码
         */
        val code_d: Int,
        /**
         * 夜间天气状况代码
         */
        val code_n: Int,
        /**
         * 白天天气状况描述
         */
        val txt_d: String,
        /**
         * 夜间天气状况描述
         */
        val txt_n: String,
        /**
         * 实时天气状况代码
         */
        val code: Int,
        /**
         * 实时天气状况描述
         */
        val txt: String
)