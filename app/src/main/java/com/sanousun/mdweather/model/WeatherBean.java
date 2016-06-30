package com.sanousun.mdweather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherBean {

    @SerializedName("city")
    @Expose
    public String city;

    @SerializedName("cityid")
    @Expose
    public String cityid;

    @SerializedName("today")
    @Expose
    public TodayEntity today;

    @SerializedName("forecast")
    @Expose
    public List<ForecastEntity> forecast;

    @SerializedName("history")
    @Expose
    public List<HistoryEntity> history;

    public static class TodayEntity {

        @SerializedName("date")
        @Expose
        public String date;

        @SerializedName("week")
        @Expose
        public String week;

        @SerializedName("curTemp")
        @Expose
        public String curTemp;

        @SerializedName("aqi")
        @Expose
        public String aqi;

        @SerializedName("fengxiang")
        @Expose
        public String fengxiang;

        @SerializedName("fengli")
        @Expose
        public String fengli;

        @SerializedName("hightemp")
        @Expose
        public String hightemp;

        @SerializedName("lowtemp")
        @Expose
        public String lowtemp;

        @SerializedName("type")
        @Expose
        public String type;

        @SerializedName("index")
        @Expose
        public List<IndexEntity> index;

        public static class IndexEntity {

            @SerializedName("name")
            @Expose
            public String name;

            @SerializedName("code")
            @Expose
            public String code;

            @SerializedName("index")
            @Expose
            public String index;

            @SerializedName("details")
            @Expose
            public String details;

            @SerializedName("otherName")
            @Expose
            public String otherName;
        }
    }

    public static class ForecastEntity {

        @SerializedName("date")
        @Expose
        public String date;

        @SerializedName("week")
        @Expose
        public String week;

        @SerializedName("fengxiang")
        @Expose
        public String fengxiang;

        @SerializedName("fengli")
        @Expose
        public String fengli;

        @SerializedName("hightemp")
        @Expose
        public String hightemp;

        @SerializedName("lowtemp")
        @Expose
        public String lowtemp;

        @SerializedName("type")
        @Expose
        public String type;
    }

    public static class HistoryEntity {

        @SerializedName("date")
        @Expose
        public String date;

        @SerializedName("week")
        @Expose
        public String week;

        @SerializedName("aqi")
        @Expose
        public String aqi;

        @SerializedName("fengxiang")
        @Expose
        public String fengxiang;

        @SerializedName("fengli")
        @Expose
        public String fengli;

        @SerializedName("hightemp")
        @Expose
        public String hightemp;

        @SerializedName("lowtemp")
        @Expose
        public String lowtemp;

        @SerializedName("type")
        @Expose
        public String type;
    }
}
