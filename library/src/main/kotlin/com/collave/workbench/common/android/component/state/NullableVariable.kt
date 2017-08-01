package com.collave.workbench.common.android.component.state

import com.collave.workbench.common.android.extension.onAndroidUI
import com.google.common.base.Optional
import io.reactivex.subjects.BehaviorSubject
import kotlin.reflect.KProperty

/**
 * Created by Andrew on 4/20/2017.
 */
class NullableVariable<T>(initial: T? = null) {

    var value: T? by this
    private val subject = BehaviorSubject.create<Optional<T>>()
    val onValueUpdated = subject.onAndroidUI()

    init {
        if (initial != null) {
            value = initial
        }
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return if (subject.hasValue()) subject.value.orNull() else null
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        subject.onNext(Optional.of(value))
    }

}