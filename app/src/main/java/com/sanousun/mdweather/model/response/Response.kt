package com.sanousun.mdweather.model.response

/**
 * Created by dashu on 2017/6/22.
 * 和风天气的返回结构
 */

data class Response<out T>(val HeWeather5: List<T>) {
    fun getData(): T {
        return HeWeather5[0]
    }
}
