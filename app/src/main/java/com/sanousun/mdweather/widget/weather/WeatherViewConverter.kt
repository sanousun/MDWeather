package com.sanousun.mdweather.widget.weather

import com.sanousun.mdweather.bg.WeatherView
import com.sanousun.mdweather.model.Weather

/**
 * @author dashu
 * @date 2018/3/29
 * 将 weatherBean 转化为 bgModule 中 weatherKindRule
 * @see com.sanousun.mdweather.bg.WeatherView
 * 数据返回code参照 http://www.heweather.com/documents/condition
 */
class WeatherViewConverter

@WeatherView.WeatherKindRule
fun Weather.convertToKindRule(): Int {
    val isDayTime = astro.isDay()
    if (isDayTime) {
        return when (cond.code_d) {
            100 -> WeatherView.WEATHER_KIND_CLEAR_DAY
            in 101..103 -> WeatherView.WEATHER_KIND_CLOUD_DAY
            104 -> WeatherView.WEATHER_KIND_CLOUDY
            in 200..213 -> WeatherView.WEATHER_KIND_WIND
            302 -> WeatherView.WEATHER_KIND_THUNDER
            303 -> WeatherView.WEATHER_KIND_THUNDERSTORM
            304 -> WeatherView.WEATHER_KIND_HAIL_DAY
            300, 301, in 305..313 -> WeatherView.WEATHER_KIND_RAINY_DAY
            in 400..407 -> WeatherView.WEATHER_KIND_SNOW_DAY
            in 500..501 -> WeatherView.WEATHER_KIND_FOG
            502 -> WeatherView.WEATHER_KIND_HAZE
            in 503..508 -> WeatherView.WEATHER_KIND_WIND
            else -> WeatherView.WEATHER_KIND_CLEAR_DAY
        }
    } else {
        return when (cond.code_n) {
            100 -> WeatherView.WEATHER_KIND_CLEAR_NIGHT
            in 101..103 -> WeatherView.WEATHER_KIND_CLOUD_NIGHT
            104 -> WeatherView.WEATHER_KIND_CLOUDY
            in 200..213 -> WeatherView.WEATHER_KIND_WIND
            302 -> WeatherView.WEATHER_KIND_THUNDER
            303 -> WeatherView.WEATHER_KIND_THUNDERSTORM
            304 -> WeatherView.WEATHER_KIND_HAIL_NIGHT
            300, 301, in 305..313 -> WeatherView.WEATHER_KIND_RAINY_NIGHT
            in 400..407 -> WeatherView.WEATHER_KIND_SNOW_NIGHT
            in 500..501 -> WeatherView.WEATHER_KIND_FOG
            502 -> WeatherView.WEATHER_KIND_HAZE
            in 503..508 -> WeatherView.WEATHER_KIND_WIND
            else -> WeatherView.WEATHER_KIND_CLEAR_DAY
        }
    }
}