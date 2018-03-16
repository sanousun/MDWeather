package com.sanousun.mdweather.model

/**
 * Created by dashu on 2017/6/16.
 * 空气质量指数数据模型
 */

data class AirQualityIndex(
        val city: Aqi
) {
    data class Aqi(
            /**
             * 空气质量指数
             */
            val aqi: Int,
            /**
             * 一氧化碳指数
             */
            val co: Float,
            /**
             * 一氧化氮指数
             */
            val no2: Int,
            /**
             * 臭氧指数
             */
            val o3: Int,
            /**
             * pm10指数
             */
            val pm10: Int,
            /**
             * pm2.5指数
             */
            val pm25: Int,
            /**
             * 空气质量，共六个级别，分别：优，良，轻度污染，中度污染，重度污染，严重污染
             */
            val qlty: String,
            /**
             * 二氧化硫含量
             */
            val so2: Int
    )
}