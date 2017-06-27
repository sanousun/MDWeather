package com.sanousun.mdweather.enums

/**
 * Created by dashu on 2017/6/24.
 * 错误代码枚举
 */

enum class ResponseStatusEnum(val status: String, val desc: String) {
    OK("ok", "数据正常"),
    INVALID_KEY("invalid key", "错误的key"),
    UNKNOWN_CITY("unknown city", "未知或错误城市"),
    NO_MORE_REQUESTS("no more requests", "超过访问次数"),
    PARAM_INVALID("param invalid", "参数错误"),
    TOO_FAST("too fast", "超过限定的QPM"),
    ANR("anr", "无响应或超时"),
    PERMISSION_DENIED("permission denied", "无访问权限"),
    UNKNOWN_ERROR("unknown error", "未知错误");

    companion object {
        fun valueOfWithStatus(status: String?): ResponseStatusEnum {
            if (status == null) return UNKNOWN_ERROR
            return values().firstOrNull { it.status == status } ?: UNKNOWN_ERROR
        }
    }
}