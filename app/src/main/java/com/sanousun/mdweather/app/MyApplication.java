package com.sanousun.mdweather.app;

import android.app.Application;

import com.sanousun.mdweather.support.db.DataSource;

public class MyApplication extends Application {

    private static DataSource sDataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        sDataSource = new DataSource(getApplicationContext());
        sDataSource.open();
    }

    public static DataSource getDataSource() {
        return sDataSource;
    }
}
