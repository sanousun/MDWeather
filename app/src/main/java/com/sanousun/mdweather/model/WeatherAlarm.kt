package com.sanousun.mdweather.model

/**
 * Created by dashu on 2017/6/16.
 * 天气预警信息数据模型，免费版没有数据
 */

data class WeatherAlarm(
        val level: String,
        val stat: String,
        val title: String,
        val txt: String,
        val type: String)
