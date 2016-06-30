package com.sanousun.mdweather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityBean {

    @SerializedName("province_cn")
    @Expose
    public String province_cn;

    @SerializedName("district_cn")
    @Expose
    public String district_cn;

    @SerializedName("name_cn")
    @Expose
    public String name_cn;

    @SerializedName("name_en")
    @Expose
    public String name_en;

    @SerializedName("area_id")
    @Expose
    public String area_id;
}
