package com.sanousun.mdweather.model

/**
 * Created by dashu on 2017/6/16.
 * 建议数据模型
 */

data class Suggestion(
        /**
         * 体感指数
         */
        val comf: KV,
        /**
         * 洗车指数
         */
        val cw: KV,
        /**
         * 穿衣指数
         */
        val drsg: KV,
        /**
         * 感冒指数
         */
        val flu: KV,
        /**
         * 运动指数
         */
        val sport: KV,
        /**
         * 旅行指数
         */
        val trav: KV,
        /**
         * 防晒指数
         */
        val uv: KV
) {
    fun getSuggestList(): List<KV> {
        val list = ArrayList<KV>()
        list.add(comf)
        list.add(cw)
        list.add(drsg)
        list.add(flu)
        list.add(sport)
        list.add(trav)
        list.add(uv)
        return list
    }
}


data class KV(
        /**
         * 感受
         */
        val brf: String,
        /**
         * 建议
         */
        val txt: String
)