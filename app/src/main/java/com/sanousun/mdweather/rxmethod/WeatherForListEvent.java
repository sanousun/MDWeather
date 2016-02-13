package com.sanousun.mdweather.rxmethod;

import com.sanousun.mdweather.model.SimpleWeather;
import com.sanousun.mdweather.support.Constant;

public class WeatherForListEvent extends Event {
    private SimpleWeather mSimpleWeather;

    public WeatherForListEvent(SimpleWeather simpleWeather, Constant.Result result) {
        this.mSimpleWeather = simpleWeather;
        this.mEventResult = result;
    }

    public SimpleWeather getSimpleWeather() {
        return mSimpleWeather;
    }
}
