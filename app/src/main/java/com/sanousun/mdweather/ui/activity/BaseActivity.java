package com.sanousun.mdweather.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getLayoutResID();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initEvent();

    protected CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        EventBus.getDefault().register(this);
        mCompositeSubscription = new CompositeSubscription();
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mCompositeSubscription.unsubscribe();
    }
}
