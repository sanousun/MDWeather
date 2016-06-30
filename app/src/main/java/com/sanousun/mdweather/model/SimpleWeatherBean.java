package com.sanousun.mdweather.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class SimpleWeatherBean implements Parcelable {

    @SerializedName("city")
    @Expose
    public String city;

    @SerializedName("pinyin")
    @Expose
    public String pinyin;

    @SerializedName("citycode")
    @Expose
    public String citycode;

    @SerializedName("date")
    @Expose
    public String date;

    @SerializedName("time")
    @Expose
    public String time;

    @SerializedName("postCode")
    @Expose
    public String postCode;

    @SerializedName("longitude")
    @Expose
    public Double longitude;

    @SerializedName("latitude")
    @Expose
    public Double latitude;

    @SerializedName("altitude")
    @Expose
    public String altitude;

    @SerializedName("weather")
    @Expose
    public String weather;

    @SerializedName("temp")
    @Expose
    public String temp;

    @SerializedName("l_tmp")
    @Expose
    public String l_tmp;

    @SerializedName("h_tmp")
    @Expose
    public String h_tmp;

    @SerializedName("WD")
    @Expose
    public String WD;

    @SerializedName("WS")
    @Expose
    public String WS;

    @SerializedName("sunrise")
    @Expose
    public String sunrise;

    @SerializedName("sunset")
    @Expose
    public String sunset;

    public int getRiseH() {
        return (sunrise.charAt(0) - '0') * 10 + sunrise.charAt(1) - '0';
    }

    public int getRiseM() {
        return (sunrise.charAt(3) - '0') * 10 + sunrise.charAt(4) - '0';
    }

    public int getSetH() {
        return (sunset.charAt(0) - '0') * 10 + sunset.charAt(1) - '0';
    }

    public int getSetM() {
        return (sunset.charAt(3) - '0') * 10 + sunset.charAt(4) - '0';
    }

    public Boolean isNight() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        if (getRiseH() > hour ||
                ((getRiseH() == hour) && (getRiseM() > minute))) {
            return true;
        }
        if (getSetH() < hour ||
                ((getSetH() == hour) && (getSetM() < minute))) {
            return true;
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.city);
        dest.writeString(this.pinyin);
        dest.writeString(this.citycode);
        dest.writeString(this.date);
        dest.writeString(this.time);
        dest.writeString(this.postCode);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeString(this.altitude);
        dest.writeString(this.weather);
        dest.writeString(this.temp);
        dest.writeString(this.l_tmp);
        dest.writeString(this.h_tmp);
        dest.writeString(this.WD);
        dest.writeString(this.WS);
        dest.writeString(this.sunrise);
        dest.writeString(this.sunset);
    }

    public SimpleWeatherBean() {
    }

    protected SimpleWeatherBean(Parcel in) {
        this.city = in.readString();
        this.pinyin = in.readString();
        this.citycode = in.readString();
        this.date = in.readString();
        this.time = in.readString();
        this.postCode = in.readString();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.altitude = in.readString();
        this.weather = in.readString();
        this.temp = in.readString();
        this.l_tmp = in.readString();
        this.h_tmp = in.readString();
        this.WD = in.readString();
        this.WS = in.readString();
        this.sunrise = in.readString();
        this.sunset = in.readString();
    }

    public static final Parcelable.Creator<SimpleWeatherBean> CREATOR = new Parcelable.Creator<SimpleWeatherBean>() {
        @Override
        public SimpleWeatherBean createFromParcel(Parcel source) {
            return new SimpleWeatherBean(source);
        }

        @Override
        public SimpleWeatherBean[] newArray(int size) {
            return new SimpleWeatherBean[size];
        }
    };
}

