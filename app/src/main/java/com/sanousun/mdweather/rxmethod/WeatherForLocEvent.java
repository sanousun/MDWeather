package com.sanousun.mdweather.rxmethod;

import com.sanousun.mdweather.model.SimpleWeather;
import com.sanousun.mdweather.support.Constant;

public class WeatherForLocEvent extends Event {
    private SimpleWeather mSimpleWeather;

    public WeatherForLocEvent(SimpleWeather simpleWeather, Constant.Result result) {
        this.mSimpleWeather = simpleWeather;
        this.mEventResult = result;
    }

    public SimpleWeather getSimpleWeather() {
        return mSimpleWeather;
    }
}
