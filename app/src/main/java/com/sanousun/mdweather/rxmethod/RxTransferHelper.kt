package com.sanousun.mdweather.rxmethod

import com.sanousun.mdweather.enums.ResponseStatusEnum
import com.sanousun.mdweather.model.response.BaseResponse
import com.sanousun.mdweather.model.response.Response
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by dashu on 2017/6/25.
 * RxJava网路回调统一处理类
 */

class RxTransferHelper {
    companion object {
        fun <T : BaseResponse> composeFilter(errReturn: ErrorReturn): FlowableTransformer<Response<T>, T> {
            return FlowableTransformer { t ->
                t.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map { response -> response.getData() }
                        .filter { response ->
                            val statusEnum = ResponseStatusEnum.valueOfWithStatus(response.status)
                            if (statusEnum == ResponseStatusEnum.OK) {
                                true
                            } else {
                                errReturn.errorStatus(statusEnum.status)
                                false
                            }
                        }
                        .doOnError {
                            errReturn.errorNetwork(it)
                        }
            }
        }
    }
}