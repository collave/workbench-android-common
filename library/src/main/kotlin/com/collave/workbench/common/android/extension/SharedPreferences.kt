package com.collave.workbench.common.android.extension

import android.content.SharedPreferences
import com.collave.workbench.common.android.component.preferences.IntPreference
import com.collave.workbench.common.android.component.preferences.StringPreference

/**
 * Created by Andrew on 6/6/2017.
 */
fun SharedPreferences.string(propertyName: String? = null, defaultValue: (()->String)? = null)
        = StringPreference(this, propertyName, defaultValue)

fun SharedPreferences.int(propertyName: String? = null, defaultValue: (()->Int)? = null)
        = IntPreference(this, propertyName, defaultValue)