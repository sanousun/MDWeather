package com.sanousun.mdweather.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by dashu on 2017/6/22.
 * url上添加天气Api的key
 */

class WeatherKeyInterceptor : Interceptor {

    companion object {
        val KEY = "255bfaa117cd42799e185957e9714916"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val httpUrlBuilder = request.url().newBuilder()
        httpUrlBuilder.addQueryParameter("key", KEY)
        val requestBuilder = request.newBuilder()
        requestBuilder.url(httpUrlBuilder.build())
        Log.e("weatherApi", httpUrlBuilder.build().toString())
        request = requestBuilder.build()
        return chain.proceed(request)
    }
}
