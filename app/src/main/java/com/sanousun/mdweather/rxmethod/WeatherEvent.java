package com.sanousun.mdweather.rxmethod;

import com.sanousun.mdweather.model.Weather;

public class WeatherEvent extends Event {

    private Weather mWeather;

    public WeatherEvent(Weather weather,int result) {
        this.mWeather = weather;
        this.mEventResult = result;
    }

    public Weather getWeather() {
        return mWeather;
    }
}
