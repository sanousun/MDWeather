package com.sanousun.mdweather.support.util;

import android.content.Context;
import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

public class WeatherIconUtil {

    static Map<String, String> sWeatherIcon = new HashMap<>();
    static Map<String, String> sWeatherBackground = new HashMap<>();

    static final String[] WEATHER_TYPE = {
            "晴", "多云", "阴", "雷阵雨", "雷阵雨伴有冰雹", "冻雨", "阵雨", "小雨", "中雨",
            "大雨", "暴雨", "大暴雨", "特大暴雨", "雨夹雪", "阵雪", "小雪", "中雪", "大雪",
            "暴雪", "雾", "沙尘暴", "强沙尘暴", "浮尘", "扬沙", "霾"};
    static final String[] WEATHER_TYPE_EN = {
            "weather_fine", "weather_cloudy", "weather_overcast", "weather_thundershower",
            "weather_thundershower_with_hail", "weather_freezing_rain", "weather_shower",
            "weather_light_rain", "weather_moderate_rain", "weather_heavy_rain",
            "weather_rainstorm", "weather_heavy_rainstorm", "weather_extraordinary_rainstorm",
            "weather_sleet", "weather_snow_shower", "weather_light_snow",
            "weather_moderate_snow", "weather_heavy_snow", "weather_snowstorm",
            "weather_fog", "weather_sandstorm", "weather_strong_sandstorm",
            "weather_floating_dust", "weather_blowing_sand", "weather_haze"};
    static final String[] WEATHER_BG = {
            "weather_fine", "weather_cloudy", "weather_overcast", "weather_thundershower",
            "weather_thundershower", "weather_rain", "weather_shower",
            "weather_rain", "weather_rain", "weather_rain",
            "weather_rain", "weather_rain", "weather_rain",
            "weather_snow_shower", "weather_snow_shower", "weather_snow",
            "weather_snow", "weather_snow", "weather_snow",
            "weather_fog", "weather_sandstorm", "weather_sandstorm",
            "weather_sandstorm", "weather_sandstorm", "weather_haze"
    };

    static {
        for (int i = 0; i < WEATHER_TYPE.length; i++) {
            sWeatherIcon.put(WEATHER_TYPE[i], WEATHER_TYPE_EN[i]);
            sWeatherBackground.put(WEATHER_TYPE[i], WEATHER_BG[i]);
        }
    }

    /**
     * 通过获得的天气type得到对应的图标资源地址
     */
    public static int getIconResId(Context context, String type) {
        Resources resources = context.getResources();
        if (sWeatherIcon.get(type) != null) {
            return resources.getIdentifier(
                    sWeatherIcon.get(type), "drawable", "com.sanousun.mdweather");
        }
        return -1;
    }

    public static int getBackgroundResId(Context context, String type, boolean isNight) {
        Resources resources = context.getResources();
        if (sWeatherBackground.get(type) != null) {
            if (isNight) {
                return resources.getIdentifier(
                        "bg_" + sWeatherBackground.get(type) + "_night",
                        "drawable", "com.sanousun.mdweather");
            } else {
                return resources.getIdentifier(
                        "bg_" + sWeatherBackground.get(type),
                        "drawable", "com.sanousun.mdweather");
            }
        }
        return -1;
    }

    public static int getBackColorResId(Context context, String type, boolean isNight) {
        Resources resources = context.getResources();
        if (sWeatherBackground.get(type) != null) {
            if (isNight) {
                return resources.getIdentifier(
                        sWeatherBackground.get(type) + "_night",
                        "color", "com.sanousun.mdweather");
            } else {
                return resources.getIdentifier(
                        sWeatherBackground.get(type), "color",
                        "com.sanousun.mdweather");
            }
        }
        return -1;
    }

    public static int getIndexResId(Context context, String index) {
        Resources resources = context.getResources();
        String s = "index_" + index;
        return resources.getIdentifier(
                s, "drawable", "com.sanousun.mdweather");
    }
}
