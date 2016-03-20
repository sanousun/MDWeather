package com.sanousun.mdweather.rxmethod;

import android.content.Context;
import android.widget.Toast;

import com.sanousun.mdweather.support.util.NetworkUtil;

public class SimpleErrorVerify implements ErrorVerify {

    private Context mContext;

    public SimpleErrorVerify(Context context) {
        mContext = context;
    }

    @Override
    public void call(String errMsg) {
        Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
        callback();
    }

    @Override
    public void errorNetwork(Throwable throwable) {
        if (NetworkUtil.isNetworkConnected(mContext)) {
            Toast.makeText(mContext, "网络异常，请稍后尝试", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "网络未连接", Toast.LENGTH_SHORT).show();
        }
        callback();
    }

    @Override
    public void callback() {

    }
}
