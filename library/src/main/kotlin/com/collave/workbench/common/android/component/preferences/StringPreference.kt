package com.collave.workbench.common.android.component.preferences

import android.content.SharedPreferences
import kotlin.reflect.KProperty

/**
 * Created by Andrew on 6/6/2017.
 */
class StringPreference(val preferences: SharedPreferences, val propertyName: String? = null, val defaultValue: (()->String)? = null) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        val name = propertyName ?: property.name
        val value = preferences.getString(name, "")
        if (value.isEmpty() && defaultValue != null) {
            val newValue = defaultValue.invoke()
            preferences.edit().putString(name, newValue).apply()
            return newValue
        }
        return value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        val name = propertyName ?: property.name
        preferences.edit().putString(name, value).apply()
    }

}