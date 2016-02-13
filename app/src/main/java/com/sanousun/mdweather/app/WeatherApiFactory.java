package com.sanousun.mdweather.app;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;


public class WeatherApiFactory {

    private static final String BASE_URL = "http://apis.baidu.com/apistore/weatherservice/";

    private static WeatherApi weatherApi;

    public static WeatherApi getWeatherApi() {
        if (weatherApi == null) {
            synchronized (WeatherApiFactory.class) {
                if (weatherApi == null) {
                    Interceptor interceptor = new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request newRequest = chain.request().
                                    newBuilder().
                                    addHeader("apikey", "994047604e192ab8aa8efcd255763f59").
                                    build();
                            return chain.proceed(newRequest);
                        }
                    };

                    OkHttpClient client =
                            new OkHttpClient.Builder().
                            addInterceptor(interceptor).
                            build();

                    Retrofit retrofit = new Retrofit.Builder().
                            baseUrl(BASE_URL).
                            addConverterFactory(GsonConverterFactory.create()).
                            addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                            client(client).
                            build();

                    weatherApi = retrofit.create(WeatherApi.class);
                }
            }
        }
        return weatherApi;
    }
}
