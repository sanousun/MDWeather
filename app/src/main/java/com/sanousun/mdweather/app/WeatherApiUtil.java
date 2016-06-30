package com.sanousun.mdweather.app;


import com.sanousun.mdweather.model.BaseResponse;
import com.sanousun.mdweather.rxmethod.ErrorVerify;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class WeatherApiUtil {

    private static final String BASE_URL = "http://apis.baidu.com/apistore/weatherservice/";

    private static WeatherApi weatherApi;

    public static WeatherApi getWeatherApi() {
        if (weatherApi == null) {
            synchronized (WeatherApiUtil.class) {
                if (weatherApi == null) {
                    Interceptor interceptor = chain -> {
                        Request newRequest = chain.request()
                                .newBuilder()
                                .addHeader("apikey", "994047604e192ab8aa8efcd255763f59")
                                .build();
                        return chain.proceed(newRequest);
                    };

                    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(interceptor)
                            .addInterceptor(httpLoggingInterceptor)
                            .build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(client)
                            .build();

                    weatherApi = retrofit.create(WeatherApi.class);
                }
            }
        }
        return weatherApi;
    }

    public static <T> Observable.Transformer<BaseResponse<T>, T> composeFilter(
            final ErrorVerify errVerify) {
        return baseResponseObservable -> baseResponseObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(
                        throwable -> {
                            errVerify.errorNetwork(throwable);
                            return null;
                        })
                .filter(baseResponse -> {
                    if (baseResponse == null) {
                        return false;
                    }
                    if (!baseResponse.isSuc()) {
                        errVerify.call(baseResponse.getErrMsg());
                        return false;
                    }
                    return true;
                })
                .map((Func1<BaseResponse<T>, T>) BaseResponse::getRetData);
    }
}
