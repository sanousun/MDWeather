package com.sanousun.mdweather.app;

import com.sanousun.mdweather.model.CityList;
import com.sanousun.mdweather.model.SimpleWeather;
import com.sanousun.mdweather.model.Weather;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface WeatherApi {

    @GET("citylist")
    Observable<CityList> getCityList(@Query("cityname") String cityName);

    @GET("recentweathers")
    Observable<Weather> getWeather(@Query("cityid") String cityId);

    @GET("cityid")
    Observable<SimpleWeather> getSimpleWeather(@Query("cityid") String cityId);

    @GET("cityname")
    Observable<SimpleWeather> getSimpleWeatherForLoc(@Query("cityname") String cityName);
}
