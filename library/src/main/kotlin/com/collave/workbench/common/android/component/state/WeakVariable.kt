package com.collave.workbench.common.android.component.state

import com.collave.workbench.common.android.extension.onAndroidUI
import com.google.common.base.Optional
import io.reactivex.subjects.PublishSubject
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

/**
 * Created by Andrew on 5/15/2017.
 */
class WeakVariable<T>(initial: T? = null) {

    var reference: WeakReference<T>? =
            if (initial == null) null
            else WeakReference<T>(initial)

    var value by this

    private val subject = PublishSubject.create<Optional<T>>()
    val onValueUpdated = subject.onAndroidUI()

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return reference?.get()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        reference = if (value == null) null else WeakReference<T>(value)
        subject.onNext(Optional.of(value))
    }

}