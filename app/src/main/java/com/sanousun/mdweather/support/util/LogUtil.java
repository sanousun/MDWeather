package com.sanousun.mdweather.support.util;

import android.text.TextUtils;
import android.util.Log;

public class LogUtil {
    static {
        debuggable = true;
    }

    public static boolean debuggable;
    private static String DEFAULT_TAG = "TAG";

    public static void v(String tag, String msg) {
        if (debuggable) {
            largeLogV(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (debuggable) {
            largeLogI(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (debuggable) {
            largeLogE(tag, msg);
        }
    }


    public static void v(String msg) {
        if (debuggable) {
            largeLogV(DEFAULT_TAG, msg);
        }
    }

    public static void i(String msg) {
        if (debuggable) {
            largeLogI(DEFAULT_TAG, msg);
        }
    }

    public static void e(String msg) {
        if (debuggable) {
            largeLogE(DEFAULT_TAG, msg);
        }
    }

    public static void largeLogI(String tag, String content) {
        if (!TextUtils.isEmpty(content)) {
            if (content.length() > 3000) {
                Log.i(tag, content.substring(0, 3000));
                largeLogI(tag, content.substring(3000));
            } else {
                Log.i(tag, content);
            }
        }
    }

    public static void largeLogV(String tag, String content) {
        if (!TextUtils.isEmpty(content)) {
            if (content.length() > 3000) {
                Log.v(tag, content.substring(0, 3000));
                largeLogV(tag, content.substring(3000));
            } else {
                Log.v(tag, content);
            }
        }
    }

    public static void largeLogE(String tag, String content) {
        if (!TextUtils.isEmpty(content)) {
            if (content.length() > 3000) {
                Log.e(tag, content.substring(0, 3000));
                largeLogE(tag, content.substring(3000));
            } else {
                Log.e(tag, content);
            }
        }
    }
}
