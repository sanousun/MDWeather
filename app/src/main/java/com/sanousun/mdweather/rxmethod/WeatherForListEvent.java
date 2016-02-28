package com.sanousun.mdweather.rxmethod;

import com.sanousun.mdweather.model.SimpleWeather;

public class WeatherForListEvent extends Event {
    private SimpleWeather mSimpleWeather;

    public WeatherForListEvent(SimpleWeather simpleWeather, int result) {
        this.mSimpleWeather = simpleWeather;
        this.mEventResult = result;
    }

    public SimpleWeather getSimpleWeather() {
        return mSimpleWeather;
    }
}
