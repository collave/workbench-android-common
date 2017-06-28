package com.collave.workbench.common.android.component.preferences

import android.content.SharedPreferences
import kotlin.reflect.KProperty

/**
 * Created by Andrew on 6/21/2017.
 */
class IntPreference(val preferences: SharedPreferences, val propertyName: String? = null, val defaultValue: (()->Int)? = null) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        val name = propertyName ?: property.name
        return preferences.getInt(name, defaultValue?.invoke() ?: 0)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        val name = propertyName ?: property.name
        preferences.edit().putInt(name, value).apply()
    }

}