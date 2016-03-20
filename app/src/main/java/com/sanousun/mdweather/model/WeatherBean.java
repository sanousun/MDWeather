package com.sanousun.mdweather.model;

import java.util.List;

public class WeatherBean {

    private String city;
    private String cityid;

    private TodayEntity today;

    private List<ForecastEntity> forecast;

    private List<HistoryEntity> history;

    public void setCity(String city) {
        this.city = city;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public void setToday(TodayEntity today) {
        this.today = today;
    }

    public void setForecast(List<ForecastEntity> forecast) {
        this.forecast = forecast;
    }

    public void setHistory(List<HistoryEntity> history) {
        this.history = history;
    }

    public String getCity() {
        return city;
    }

    public String getCityid() {
        return cityid;
    }

    public TodayEntity getToday() {
        return today;
    }

    public List<ForecastEntity> getForecast() {
        return forecast;
    }

    public List<HistoryEntity> getHistory() {
        return history;
    }

    public static class TodayEntity {
        private String date;
        private String week;
        private String curTemp;
        private String aqi;
        private String fengxiang;
        private String fengli;
        private String hightemp;
        private String lowtemp;
        private String type;

        private List<IndexEntity> index;

        public void setDate(String date) {
            this.date = date;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public void setCurTemp(String curTemp) {
            this.curTemp = curTemp;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public void setFengxiang(String fengxiang) {
            this.fengxiang = fengxiang;
        }

        public void setFengli(String fengli) {
            this.fengli = fengli;
        }

        public void setHightemp(String hightemp) {
            this.hightemp = hightemp;
        }

        public void setLowtemp(String lowtemp) {
            this.lowtemp = lowtemp;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setIndex(List<IndexEntity> index) {
            this.index = index;
        }

        public String getDate() {
            return date;
        }

        public String getWeek() {
            return week;
        }

        public String getCurTemp() {
            return curTemp;
        }

        public String getAqi() {
            return aqi;
        }

        public String getFengxiang() {
            return fengxiang;
        }

        public String getFengli() {
            return fengli;
        }

        public String getHightemp() {
            return hightemp;
        }

        public String getLowtemp() {
            return lowtemp;
        }

        public String getType() {
            return type;
        }

        public List<IndexEntity> getIndex() {
            return index;
        }

        public static class IndexEntity {
            private String name;
            private String code;
            private String index;
            private String details;
            private String otherName;

            public void setName(String name) {
                this.name = name;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public void setIndex(String index) {
                this.index = index;
            }

            public void setDetails(String details) {
                this.details = details;
            }

            public void setOtherName(String otherName) {
                this.otherName = otherName;
            }

            public String getName() {
                return name;
            }

            public String getCode() {
                return code;
            }

            public String getIndex() {
                return index;
            }

            public String getDetails() {
                return details;
            }

            public String getOtherName() {
                return otherName;
            }
        }
    }

    public static class ForecastEntity {
        private String date;
        private String week;
        private String fengxiang;
        private String fengli;
        private String hightemp;
        private String lowtemp;
        private String type;

        public void setDate(String date) {
            this.date = date;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public void setFengxiang(String fengxiang) {
            this.fengxiang = fengxiang;
        }

        public void setFengli(String fengli) {
            this.fengli = fengli;
        }

        public void setHightemp(String hightemp) {
            this.hightemp = hightemp;
        }

        public void setLowtemp(String lowtemp) {
            this.lowtemp = lowtemp;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDate() {
            return date;
        }

        public String getWeek() {
            return week;
        }

        public String getFengxiang() {
            return fengxiang;
        }

        public String getFengli() {
            return fengli;
        }

        public String getHightemp() {
            return hightemp;
        }

        public String getLowtemp() {
            return lowtemp;
        }

        public String getType() {
            return type;
        }
    }

    public static class HistoryEntity {
        private String date;
        private String week;
        private String aqi;
        private String fengxiang;
        private String fengli;
        private String hightemp;
        private String lowtemp;
        private String type;

        public void setDate(String date) {
            this.date = date;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public void setFengxiang(String fengxiang) {
            this.fengxiang = fengxiang;
        }

        public void setFengli(String fengli) {
            this.fengli = fengli;
        }

        public void setHightemp(String hightemp) {
            this.hightemp = hightemp;
        }

        public void setLowtemp(String lowtemp) {
            this.lowtemp = lowtemp;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDate() {
            return date;
        }

        public String getWeek() {
            return week;
        }

        public String getAqi() {
            return aqi;
        }

        public String getFengxiang() {
            return fengxiang;
        }

        public String getFengli() {
            return fengli;
        }

        public String getHightemp() {
            return hightemp;
        }

        public String getLowtemp() {
            return lowtemp;
        }

        public String getType() {
            return type;
        }
    }
}
