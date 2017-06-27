package com.sanousun.mdweather.model

/**
 * Created by dashu on 2017/6/16.
 * 风数据模型
 */

data class Wind(
        /**
         * 风向（360度）
         */
        val deg: Int,
        /**
         * 风向
         */
        val dir: String,
        /**
         * 风力等级
         */
        val sc: String,
        /**
         * 风速（kmph）
         */
        val spd: Int
)