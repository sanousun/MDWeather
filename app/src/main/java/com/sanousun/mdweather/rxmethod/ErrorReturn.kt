package com.sanousun.mdweather.rxmethod

/**
 * Created by dashu on 2017/6/25.
 * 网络请求错误回调接口定义
 */

interface ErrorReturn {
    /**
     * 业务错误处理
     */
    fun errorStatus(status: String)

    /**
     * 网络错误处理
     */
    fun errorNetwork(th: Throwable)
}