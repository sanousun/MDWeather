package com.sanousun.mdweather.model

/**
 * Created by dashu on 2017/6/16.
 * 城市基本信息数据模型
 */

data class BasicCity(
        /**
         * 城市id
         */
        val id: String,
        /**
         * 城市名
         */
        val city: String,
        /**
         * 城市所属省份名（仅限国内城市）
         */
        val prov: String,
        /**
         * 国家名
         */
        val cnty: String,
        /**
         * 城市纬度
         */
        val lat: String,
        /**
         * 城市经度
         */
        val lon: String,
        /**
         * 时间
         */
        val update: UpdateTime
) {
    fun getLocationName(): String {
        if (prov == city) {
            return "$cnty $city"
        } else {
            return "$cnty $prov $city"
        }
    }
}