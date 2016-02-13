package com.sanousun.mdweather.rxmethod;

import com.sanousun.mdweather.model.CityList;
import com.sanousun.mdweather.support.Constant.Result;

public class CityListEvent extends Event {

    private CityList mCityList;

    public CityListEvent(CityList cityList, Result result) {
        this.mCityList = cityList;
        this.mEventResult = result;
    }

    public CityList getCityList() {
        return mCityList;
    }
}
