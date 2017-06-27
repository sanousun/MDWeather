package com.sanousun.mdweather.model

/**
 * Created by dashu on 2017/6/16.
 * 更新时间数据模型
 */

data class UpdateTime(
        /**
         * 当地时间 yyyy-MM-dd hh:mm
         */
        val loc: String,
        /**
         * UTC时间 yyyy-MM-dd hh:mm
         */
        val utc: String
)