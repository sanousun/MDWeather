package com.sanousun.mdweather.rxmethod;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.sanousun.mdweather.support.util.NetworkUtil;

public class SimpleErrorVerify implements ErrorVerify {

    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public SimpleErrorVerify(Context context, SwipeRefreshLayout layout) {
        mContext = context;
        mSwipeRefreshLayout = layout;
    }

    @Override
    public void call(String errMsg) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void errorNetwork(Throwable throwable) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (NetworkUtil.isNetworkConnected(mContext)) {
            Toast.makeText(mContext, "网络异常，请稍后尝试", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "网络未连接", Toast.LENGTH_SHORT).show();
        }
    }
}
