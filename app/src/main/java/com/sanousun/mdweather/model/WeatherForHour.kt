package com.sanousun.mdweather.model

/**
 * Created by dashu on 2017/6/16.
 * 小时天气数据模型，tmp为Int型数值
 */

data class WeatherForHour(
        val astro: Astronomy,
        /**
         * 天气状况
         */
        val cond: WeatherCondition,
        /**
         * 日期 yyyy-MM-dd
         */
        val date: String,
        /**
         * 体感温度
         */
        val fl: Int,
        /**
         * 相对湿度（%）
         */
        val hum: Int,
        /**
         * 降水量（mm）
         */
        val pcpn: Float,
        /**
         * 降水概率
         */
        val pop: Int,
        /**
         * 气压
         */
        val pres: Int,
        /**
         * 温度
         */
        val tmp: Int,
        /**
         * 能见度（km）
         */
        val vis: Int,
        /**
         * 风
         */
        val wind: Wind
)
