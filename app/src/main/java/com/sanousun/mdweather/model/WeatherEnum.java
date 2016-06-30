package com.sanousun.mdweather.model;

import android.content.Context;
import android.content.res.Resources;

public enum WeatherEnum {

    fine("晴", "weather_fine", "weather_fine"),
    cloudy("多云", "weather_cloudy", "weather_cloudy"),
    overcast("阴", "weather_overcast", "weather_overcast"),
    thundershower("雷阵雨", "weather_thundershower", "weather_thundershower"),
    thundershower_with_hail("雷阵雨伴有冰雹", "weather_thundershower_with_hail", "weather_thundershower"),
    freezing_rain("冻雨", "weather_freezing_rain", "weather_rain"),
    shower("阵雨", "weather_shower", "weather_shower"),
    light_rain("小雨", "weather_light_rain", "weather_rain"),
    moderate_rain("中雨", "weather_moderate_rain", "weather_rain"),
    heavy_rain("大雨", "weather_heavy_rain", "weather_rain"),
    rainstorm("暴雨", "weather_rainstorm", "weather_rain"),
    heavy_rainstorm("大暴雨", "weather_heavy_rainstorm", "weather_rain"),
    extraordinary_rainstorm("特大暴雨", "weather_extraordinary_rainstorm", "weather_rain"),
    sleet("雨夹雪", "weather_sleet", "weather_snow_shower"),
    snow_shower("阵雪", "weather_snow_shower", "weather_snow_shower"),
    light_snow("小雪", "weather_light_snow", "weather_snow"),
    moderate_snow("中雪", "weather_moderate_snow", "weather_snow"),
    heavy_snow("大雪", "weather_heavy_snow", "weather_snow"),
    snowstorm("暴雪", "weather_snowstorm", "weather_snow"),
    fog("雾", "weather_fog", "weather_fog"),
    sandstorm("沙尘暴", "weather_sandstorm", "weather_sandstorm"),
    strong_sandstorm("强沙尘暴", "weather_strong_sandstorm", "weather_sandstorm"),
    floating_dust("浮尘", "weather_floating_dust", "weather_sandstorm"),
    blowing_sand("扬沙", "weather_blowing_sand", "weather_sandstorm"),
    weather_haze("霾", "weather_haze", "weather_haze");

    private String weather;
    private String icon;
    private String background;

    WeatherEnum(String weather, String icon, String background) {
        this.weather = weather;
        this.icon = icon;
        this.background = background;
    }

    public int getIcon(Context context) {
        Resources resources = context.getResources();
        return resources.getIdentifier(icon, "drawable", "com.sanousun.mdweather");
    }

    public int getBackground(Context context) {
        Resources resources = context.getResources();
        return resources.getIdentifier("bg_" + background, "drawable", "com.sanousun.mdweather");
    }

    public int getBackgroundForNight(Context context) {
        Resources resources = context.getResources();
        return resources.getIdentifier("bg_" + background + "_n", "drawable", "com.sanousun.mdweather");
    }

    public int getColor(Context context) {
        Resources resources = context.getResources();
        return resources.getIdentifier(icon, "color", "com.sanousun.mdweather");
    }

    public int getColorForNight(Context context) {
        Resources resources = context.getResources();
        return resources.getIdentifier(icon + "_n", "color", "com.sanousun.mdweather");
    }
}
