package com.collave.workbench.common.android.component.state

import com.collave.workbench.common.android.extension.onAndroidUI
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

/**
 * Created by Andrew on 4/25/2017.
 */
class StatefulDataPublishRequest<T>(private val creator: (Array<out Any?>)-> Single<T>) : StatefulRequest<T>() {

    data class SingleResult<T>(val state: State, val data: T?, val error: Throwable?)
    private val subject = PublishSubject.create<T>()
    val onValuePublished = subject.onAndroidUI()

    override fun createRequest(vararg args: Any?) = creator.invoke(args)
    override fun handleResult(result: T) {
        subject.onNext(result)
    }

    fun publishData(value: T) {
        subject.onNext(value)
    }

}