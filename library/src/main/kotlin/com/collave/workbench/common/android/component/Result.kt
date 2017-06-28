package com.collave.workbench.common.android.component

import io.reactivex.Single

/**
 * Created by Andrew on 6/5/2017.
 */
fun <T: Any> Single<T>.toSingleResult(): Single<Result<T>> {
    return map { Result.success(it) }.onErrorReturn { Result.error<T>(it) }
}

class Result<T: Any> {

    companion object {
        fun <T: Any> success(data: T) =  Result(data)
        fun <T: Any> error(error: Throwable) =  Result<T>(error)
    }

    constructor(data: T) {
        this.data = data
        isSuccess = true
    }

    constructor(error: Throwable) {
        this.error = error
        isError = true
    }

    lateinit var data: T
        private set

    var error: Throwable? = null
        private set

    var isError: Boolean = false
        private set

    var isSuccess: Boolean = false
        private set

}