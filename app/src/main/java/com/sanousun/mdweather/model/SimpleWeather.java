package com.sanousun.mdweather.model;

import java.util.Calendar;

public class SimpleWeather {

    private int errNum;
    private String errMsg;

    private RetDataEntity retData;

    public void setErrNum(int errNum) {
        this.errNum = errNum;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public void setRetData(RetDataEntity retData) {
        this.retData = retData;
    }

    public int getErrNum() {
        return errNum;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public RetDataEntity getRetData() {
        return retData;
    }

    public static class RetDataEntity {
        private String city;
        private String pinyin;
        private String citycode;
        private String date;
        private String time;
        private String postCode;
        private double longitude;
        private double latitude;
        private String altitude;
        private String weather;
        private String temp;
        private String l_tmp;
        private String h_tmp;
        private String WD;
        private String WS;
        private String sunrise;
        private String sunset;

        public void setCity(String city) {
            this.city = city;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public void setCitycode(String citycode) {
            this.citycode = citycode;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public void setAltitude(String altitude) {
            this.altitude = altitude;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public void setL_tmp(String l_tmp) {
            this.l_tmp = l_tmp;
        }

        public void setH_tmp(String h_tmp) {
            this.h_tmp = h_tmp;
        }

        public void setWD(String WD) {
            this.WD = WD;
        }

        public void setWS(String WS) {
            this.WS = WS;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public String getCity() {
            return city;
        }

        public String getPinyin() {
            return pinyin;
        }

        public String getCitycode() {
            return citycode;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public String getPostCode() {
            return postCode;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public String getAltitude() {
            return altitude;
        }

        public String getWeather() {
            return weather;
        }

        public String getTemp() {
            return temp;
        }

        public String getL_tmp() {
            return l_tmp;
        }

        public String getH_tmp() {
            return h_tmp;
        }

        public String getWD() {
            return WD;
        }

        public String getWS() {
            return WS;
        }

        public String getSunrise() {
            return sunrise;
        }

        public String getSunset() {
            return sunset;
        }

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
    }
}
