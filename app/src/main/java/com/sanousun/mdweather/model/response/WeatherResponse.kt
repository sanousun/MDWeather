package com.sanousun.mdweather.model.response

import com.sanousun.mdweather.model.*

/**
 * Created by dashu on 2017/6/22.
 * 天气接口返回数据
 */

data class WeatherResponse(
        override val status: String,
        /**
         * 城市状况
         */
        val basic: BasicCity?,
        /**
         * 当前的天气状况
         */
        val now: WeatherForHour?,
        /**
         * 小时天气预报
         */
        val hourly_forecast: List<WeatherForHour>?,
        /**
         * 七日天气预报
         */
        val daily_forecast: List<Weather>?,
        /**
         * 空气质量
         */
        val aqi: AirQualityIndex?,
        /**
         * 生活指数建议
         */
        val suggestion: Suggestion?) : BaseResponse