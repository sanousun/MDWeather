package com.sanousun.mdweather.rxmethod;

import android.util.Log;

import com.sanousun.mdweather.app.WeatherApiFactory;
import com.sanousun.mdweather.model.BaseResponse;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 数据有问题，当返回错误时会返回数组导致gson解析错误
 */
public class RxMethod {

    public static Subscription getCityList(String cityName, ErrorVerify errorVerify) {
        return WeatherApiFactory.getWeatherApi().getCityList(cityName).
                compose(composeFilter(errorVerify)).
                subscribe(new Action1<BaseResponse>() {
                    @Override
                    public void call(BaseResponse baseResponse) {
                        EventBus.getDefault().post(baseResponse.getRetData());
                    }
                });
    }

    public static Subscription getSimpleWeather(String cityId, ErrorVerify errorVerify) {
        return WeatherApiFactory.getWeatherApi().getSimpleWeather(cityId).
                compose(composeFilter(errorVerify)).
                subscribe(new Action1<BaseResponse>() {
                    @Override
                    public void call(BaseResponse baseResponse) {
                        EventBus.getDefault().post(baseResponse.getRetData());
                    }
                });
    }

    public static Subscription getWeather(String cityId, ErrorVerify errorVerify) {
        return WeatherApiFactory.getWeatherApi().getWeather(cityId).
                compose(composeFilter(errorVerify)).
                subscribe(new Action1<BaseResponse>() {
                    @Override
                    public void call(BaseResponse baseResponse) {
                        EventBus.getDefault().post(baseResponse.getRetData());
                    }
                });
    }

    public static Subscription getWeatherForList(String cityId, ErrorVerify errorVerify) {
        return WeatherApiFactory.getWeatherApi().getSimpleWeatherForList(cityId).
                compose(composeFilter(errorVerify)).
                subscribe(new Action1<BaseResponse>() {
                    @Override
                    public void call(BaseResponse baseResponse) {
                        EventBus.getDefault().post(baseResponse.getRetData());
                    }
                });
    }

    public static Subscription getWeatherForLoc(String cityName, ErrorVerify errorVerify) {
        return WeatherApiFactory.getWeatherApi().getSimpleWeatherForLoc(cityName).
                compose(composeFilter(errorVerify)).
                subscribe(new Action1<BaseResponse>() {
                    @Override
                    public void call(BaseResponse baseResponse) {
                        EventBus.getDefault().post(baseResponse.getRetData());
                    }
                });
    }

    private static Func1<BaseResponse, Boolean> errorFilter(final ErrorVerify errorVerify) {
        return new Func1<BaseResponse, Boolean>() {
            @Override
            public Boolean call(BaseResponse baseResponse) {
                if (baseResponse == null) {
                    return false;
                }
                if (!baseResponse.isSuc()) {
                    errorVerify.call(baseResponse.getErrMsg());
                    Log.i("xyz", baseResponse.getErrMsg());
                    return false;
                }
                return true;
            }
        };
    }

    private static Observable.Transformer<BaseResponse, BaseResponse> composeFilter(
            final ErrorVerify errVerify) {
        return new Observable.Transformer<BaseResponse, BaseResponse>() {
            @Override
            public Observable<BaseResponse> call(Observable<BaseResponse> baseResponseObservable) {
                return baseResponseObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .onErrorReturn(
                                new Func1<Throwable, BaseResponse>() {
                                    @Override
                                    public BaseResponse call(Throwable throwable) {
                                        Log.i("xyz", throwable.toString());
                                        errVerify.errorNetwork(throwable);
                                        return null;
                                    }
                                })
                        .filter(errorFilter(errVerify));
            }
        };
    }

}
