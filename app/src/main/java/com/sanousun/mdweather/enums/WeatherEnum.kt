package com.sanousun.mdweather.enums

/**
 * Created by dashu on 2017/6/17.
 * 天气状况枚举类
 */

enum class WeatherEnum(
        /**
         * 天气状况代码
         */
        val code: Int,
        /**
         * 天气状况中文描述
         */
        val descEn: String,
        /**
         * 天气状况英文描述
         */
        val descCn: String,
        /**
         * 天气状况icon图标
         */
        val iconSrc: Int) {

    SUNNY(100, "晴", "Sunny", 0),
    CLOUDY(101, "多云", "Cloudy", 0),
    FEW_CLOUDS(102, "少云", "Few Clouds", 0),
    PARTLY_CLOUDY(103, "晴间多云", "Partly Cloudy", 0),
    OVERCAST(104, "阴", "Overcast", 0),
    WINDY(200, "有风", "Windy", 0),
    CALM(201, "平静", "Calm", 0),
    LIGHT_BREEZE(202, "微风", "Light Breeze", 0),
    GENTLE_BREEZE(203, "和风", "Gentle Breeze", 0),
    FRESH_BREEZE(204, "清风", "Fresh Breeze", 0),
    STRONG_BREEZE(205, "劲风", "Strong Breeze", 0),
    NEAR_GALE(206, "疾风", "Near Gale", 0),
    GALE(207, "大风", "Gale", 0),
    STRONG_GALE(208, "烈风", "Strong Gale", 0),
    STORM(209, "风暴", "Storm", 0),
    VIOLENT_STORM(210, "狂爆风", "Violent Storm", 0),
    HURRICANE(211, "飓风", "Hurricane", 0),
    TORNADO(212, "龙卷风", "Tornado", 0),
    TROPICAL_STORM(213, "热带风暴", "Tropical Storm", 0),
    SHOWER_RAIN(300, "阵雨", "Shower Rain", 0),
    HEAVY_SHOWER_RAIN(301, "强阵雨", "Heavy Shower Rain", 0),
    THUNDERSHOWER(302, "雷阵雨", "Thundershower", 0),
    HEAVY_THUNDERSHOWER(303, "强雷阵雨", "Heavy Thunderstorm", 0),
    HAIL(304, "雷阵雨伴有冰雹", "Hail", 0),
    LIGHT_RAIN(305, "小雨", "Light Rain", 0),
    MODERATE_RAIN(306, "中雨", "Moderate Rain", 0),
    HEAVY_RAIN(307, "大雨", "Heavy Rain", 0),
    EXTREME_RAIN(308, "极端降雨", "Extreme Rain", 0),
    DRIZZLE_RAIN(309, "细雨", "Drizzle Rain", 0),
    RAINSTORM(310, "暴雨", "Rainstorm", 0),
    HEAVY_RAINSTORM(311, "大暴雨", "Heavy Rainstorm", 0),
    SEVERE_RAINSTORM(312, "特大暴雨", "Severe Rainstorm", 0),
    FREEZING_RAIN(313, "冻雨", "Freezing Rain", 0),
    LIGHT_SNOW(400, "小雪", "Light Snow", 0),
    MODERATE_SNOW(401, "中雪", "Moderate Snow", 0),
    HEAVY_SNOW(402, "大雪", "Heavy Snow", 0),
    SNOWSTORM(403, "暴雪", "Snowstorm", 0),
    SLEET(404, "雨夹雪", "Sleet", 0),
    RAIN_AND_SNOW(405, "雨雪天气", "Rain And Snow", 0),
    SHOWER_SNOW(406, "阵雨夹雪", "Shower Snow", 0),
    SNOW_FLURRY(407, "阵雪", "Snow Flurry", 0),
    MIST(500, "薄雾", "Mist", 0),
    FOGGY(501, "雾", "Foggy", 0),
    HAZE(502, "霾", "Haze", 0),
    SAND(503, "扬沙", "Sand", 0),
    DUST(504, "浮尘", "Dust", 0),
    DUST_STORM(505, "沙尘暴", "Dust Storm", 0),
    SANDSTORM(506, "强沙尘暴", "Sandstorm", 0),
    HOT(900, "热", "Hot", 0),
    COLD(901, "冷", "Cold", 0),
    UNKNOWN(999, "未知", "Unknown", 0);
}