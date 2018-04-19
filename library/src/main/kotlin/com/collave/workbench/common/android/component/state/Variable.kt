package com.collave.workbench.common.android.component.state

import com.collave.workbench.common.android.extension.onAndroidUI
import io.reactivex.subjects.BehaviorSubject
import kotlin.reflect.KProperty

/**
 * Created by Andrew on 4/20/2017.
 */
class Variable<T>(default: T) {

    var value: T by this
    private val subject = BehaviorSubject.createDefault(default)
    val onValueUpdated = subject.onAndroidUI()

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return subject.value!!
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        subject.onNext(value)
    }

}