package com.sanousun.mdweather.rxmethod;

import com.sanousun.mdweather.model.SimpleWeather;

public class SimpleWeatherEvent extends Event {
    private SimpleWeather mSimpleWeather;

    public SimpleWeatherEvent(SimpleWeather simpleWeather,int result) {
        this.mSimpleWeather = simpleWeather;
        this.mEventResult = result;
    }

    public SimpleWeather getSimpleWeather() {
        return mSimpleWeather;
    }
}
