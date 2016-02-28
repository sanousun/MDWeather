package com.sanousun.mdweather.rxmethod;

import com.sanousun.mdweather.app.WeatherApiFactory;
import com.sanousun.mdweather.model.CityList;
import com.sanousun.mdweather.model.SimpleWeather;
import com.sanousun.mdweather.model.Weather;

import de.greenrobot.event.EventBus;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RxMethod {

    public static Subscription getCityList(String cityName) {
        Subscription subscription = WeatherApiFactory.getWeatherApi().getCityList(cityName).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<CityList>() {
                    @Override
                    public void call(CityList cityList) {
                        EventBus.getDefault().post(new CityListEvent(cityList, Event.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        EventBus.getDefault().post(new CityListEvent(null,Event.FAIL));
                    }
                });
        return subscription;

    }

    public static Subscription getSimpleWeather(String cityId) {
        Subscription subscription = WeatherApiFactory.getWeatherApi().getSimpleWeather(cityId).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<SimpleWeather>() {
                    @Override
                    public void call(SimpleWeather simpleWeather) {
                        EventBus.getDefault().post(new SimpleWeatherEvent(simpleWeather, Event.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        EventBus.getDefault().post(new SimpleWeatherEvent(null, Event.FAIL));
                    }
                });
        return subscription;
    }

    public static Subscription getWeather(String cityId) {
        Subscription subscription = WeatherApiFactory.getWeatherApi().getWeather(cityId).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<Weather>() {
                    @Override
                    public void call(Weather weather) {
                        EventBus.getDefault().post(new WeatherEvent(weather, Event.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        EventBus.getDefault().post(new WeatherEvent(null, Event.FAIL));
                    }
                });
        return subscription;
    }

    public static Subscription getWeatherForList(String cityId) {
        Subscription subscription = WeatherApiFactory.getWeatherApi().getSimpleWeather(cityId).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<SimpleWeather>() {
                    @Override
                    public void call(SimpleWeather simpleWeather) {
                        EventBus.getDefault().post(new WeatherForListEvent(simpleWeather, Event.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        EventBus.getDefault().post(new WeatherForListEvent(null, Event.FAIL));
                    }
                });
        return subscription;
    }

    public static Subscription getWeatherForLoc(String cityName) {
        Subscription subscription = WeatherApiFactory.getWeatherApi().getSimpleWeatherForLoc(cityName).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<SimpleWeather>() {
                    @Override
                    public void call(SimpleWeather simpleWeather) {
                        EventBus.getDefault().post(new WeatherForLocEvent(simpleWeather, Event.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        EventBus.getDefault().post(new WeatherForLocEvent(null, Event.FAIL));
                    }
                });
        return subscription;
    }
}
