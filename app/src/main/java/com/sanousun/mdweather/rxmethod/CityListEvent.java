package com.sanousun.mdweather.rxmethod;

import com.sanousun.mdweather.model.CityList;

public class CityListEvent extends Event {

    private CityList mCityList;

    public CityListEvent(CityList cityList, int result) {
        this.mCityList = cityList;
        this.mEventResult = result;
    }

    public CityList getCityList() {
        return mCityList;
    }
}
