package com.sanousun.mdweather.model.response

import com.sanousun.mdweather.model.BasicCity

/**
 * Created by dashu on 2017/6/22.
 * 天气接口返回数据
 */

data class CityResponse(
        override val status: String,
        /**
         * 城市状况
         */
        val basic: BasicCity?) : BaseResponse