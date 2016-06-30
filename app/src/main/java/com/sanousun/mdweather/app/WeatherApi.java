package com.sanousun.mdweather.app;

import com.sanousun.mdweather.model.BaseResponse;
import com.sanousun.mdweather.model.CityBean;
import com.sanousun.mdweather.model.SimpleWeatherBean;
import com.sanousun.mdweather.model.WeatherBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface WeatherApi {

    @GET("citylist")
    Observable<BaseResponse<List<CityBean>>> getCityList(@Query("cityname") String cityName);

    @GET("recentweathers")
    Observable<BaseResponse<WeatherBean>> getWeather(@Query("cityid") String cityId);

    @GET("cityid")
    Observable<BaseResponse<SimpleWeatherBean>> getSimpleWeather(@Query("cityid") String cityId);

    @GET("cityname")
    Observable<BaseResponse<SimpleWeatherBean>> getSimpleWeatherForLoc(@Query("cityname") String cityName);
}
