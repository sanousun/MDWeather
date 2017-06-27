package com.sanousun.mdweather.rxmethod

import com.sanousun.mdweather.enums.ResponseStatusEnum
import com.sanousun.mdweather.model.response.BaseResponse
import com.sanousun.mdweather.model.response.Response
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by dashu on 2017/6/25.
 * RxJava网路回调统一处理类
 */

class RxTransferHelper {
    companion object {
        fun <T : BaseResponse> composeFilter(errReturn: ErrorReturn): Observable.Transformer<Response<T>, T> {
            return Observable.Transformer {
                t ->
                t.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .onErrorReturn { throwable ->
                            errReturn.errorNetwork(throwable)
                            null
                        }
                        .filter { t ->
                            t != null
                        }
                        .map { t -> t.getData() }
                        .filter { t ->
                            val statusEnum = ResponseStatusEnum.valueOfWithStatus(t.status)
                            if (statusEnum == ResponseStatusEnum.OK) {
                                true
                            } else {
                                errReturn.errorStatus(statusEnum.status)
                                false
                            }
                        }
            }
        }
    }
}
