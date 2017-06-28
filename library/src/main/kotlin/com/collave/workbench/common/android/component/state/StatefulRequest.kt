package com.collave.workbench.common.android.component.state

import io.reactivex.Single

/**
 * Created by Andrew on 4/20/2017.
 */
abstract class StatefulRequest<T> {

    enum class State {
        Idle, InProgress, RequestError, Successful;

        val isIdle: Boolean get() = this == Idle
        val isInProgress: Boolean get() = this == InProgress
        val isRequestError: Boolean get() = this == RequestError
        val isSuccessful: Boolean get() = this == Successful
    }

    val lastErrorVariable = NullableVariable<Throwable>()
    var lastError by lastErrorVariable
        private set

    val stateVariable = Variable(State.Idle)
    var state by stateVariable
        private set

    val onStateUpdated get() = stateVariable.onValueUpdated
    val onErrorReceived get() = lastErrorVariable.onValueUpdated

    abstract fun createRequest(vararg args: Any?): Single<T>
    abstract fun handleResult(result: T)

    fun execute(vararg args: Any?) {
        if (state == State.InProgress) return
        state = State.InProgress
        createRequest(*args).subscribe { result, error ->
            if (error != null || result == null) {
                lastError = error
                state = State.RequestError
                return@subscribe
            }
            handleResult(result)
            state = State.Successful
        }
    }

}