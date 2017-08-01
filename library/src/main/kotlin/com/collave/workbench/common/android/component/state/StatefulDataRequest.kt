package com.collave.workbench.common.android.component.state

import io.reactivex.Single
import kotlin.reflect.KProperty

/**
 * Created by Andrew on 4/20/2017.
 */
open class StatefulDataRequest<T> : StatefulRequest<T> {

    protected var creator: (Array<out Any?>)-> Single<T>
    protected var initial: ((Array<out Any?>)-> Single<T>)? = null

    constructor(creator: (Array<out Any?>)-> Single<T>): super() {
        this.creator = creator
    }

    constructor(creator: (Array<out Any?>)-> Single<T>,
                initial: (Array<out Any?>)-> Single<T>): this(creator) {
        this.initial = initial
    }

    val dataVariable = NullableVariable<T>()
    var data by dataVariable
        private set

    val onDataUpdated get() = dataVariable.onValueUpdated

    override fun createRequest(vararg args: Any?) = if (data == null) createInitialRequest(*args) else creator.invoke(args)
    open fun createInitialRequest(vararg args: Any?) = initial?.invoke(args) ?: creator.invoke(args)

    override fun handleResult(result: T) {
        data = result
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return data
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        data = value
    }

    fun updateData(value: T) {
        data = value
    }

    fun executeInitial(vararg args: Any?) {
        if (data == null) {
            execute(*args)
        }
    }

    fun executeSingle(vararg args: Any?): Single<T?> {
        val single = stateVariable
                .onValueUpdated
                .skipWhile { it == State.InProgress }
                .firstOrError()
                .map {
                    if (it == State.RequestError) {
                        throw lastError ?: Exception()
                    }
                    data!!
                }

        execute(*args)
        return single
    }

}