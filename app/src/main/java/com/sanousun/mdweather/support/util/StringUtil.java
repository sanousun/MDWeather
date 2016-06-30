package com.sanousun.mdweather.support.util;

public class StringUtil {
    public static String getWeekDay(String d) {
        if (d.equals("星期天")) return "周日";
        return "周" + d.charAt(2);
    }

    public static String getDay(String d) {
        if (d.length() < 10) return "";
        StringBuilder s = new StringBuilder();
        for (int i = 5; i < 10; i++) {
            if (i == 7) {
                s.append('/');
            } else if (d.charAt(i) != '0') {
                s.append(d.charAt(i));
            }
        }
        return s.toString();
    }

    public static int getTemp(String t) {
        int tp = 0;
        boolean isLess = false;
        for (int i = 0; i < t.length() - 1; i++) {
            if (t.charAt(i) == '-') {
                isLess = true;
            } else {
                tp = tp * 10 + t.charAt(i) - '0';
            }
        }
        return (isLess ? -tp : tp);
    }

    public static int getAqi(String a) {
        if (a == null) return -1;
        int aqi = 0;
        for (int i = 0; i < a.length(); i++) {
            aqi = aqi * 10 + a.charAt(i) - '0';
        }
        return aqi;
    }
}
