package com.sanousun.mdweather.rxmethod;

public interface ErrorVerify {
    void call(String errMsg);

    void errorNetwork(Throwable throwable);

    void callback();
}
