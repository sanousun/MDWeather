package com.sanousun.mdweather.rxmethod;

import com.sanousun.mdweather.model.Weather;
import com.sanousun.mdweather.support.Constant.Result;

public class WeatherEvent extends Event {

    private Weather mWeather;

    public WeatherEvent(Weather weather, Result result) {
        this.mWeather = weather;
        this.mEventResult = result;
    }

    public Weather getWeather() {
        return mWeather;
    }
}
