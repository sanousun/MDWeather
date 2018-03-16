package com.sanousun.mdweather.network

import com.sanousun.mdweather.model.response.CityResponse
import com.sanousun.mdweather.model.response.Response
import com.sanousun.mdweather.model.response.WeatherResponse
import io.reactivex.Flowable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by dashu on 2017/6/16.
 * 天气接口定义
 */

public interface WeatherApiService {

    /**
     * 查询全部天气
     * city可通过城市中英文名称、ID、IP和经纬度进行查询，经纬度查询格式为：经度,纬度
     */
    @GET("weather")
    fun getWeatherByCity(@Query("city") city: String): Flowable<Response<WeatherResponse>>

    /**
     * city可通过城市中英文名称、ID、IP和经纬度进行查询，经纬度查询格式为：经度,纬度
     */
    @GET("search")
    fun searchCity(@Query("city") city: String): Flowable<Response<CityResponse>>

    companion object Factory {
        fun create(): WeatherApiService {
            val okhttpClient: OkHttpClient = OkHttpClient.Builder()
                    .addInterceptor(WeatherKeyInterceptor())
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://free-api.heweather.com/v5/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttpClient)
                    .build()
            return retrofit.create(WeatherApiService::class.java)
        }
    }
}