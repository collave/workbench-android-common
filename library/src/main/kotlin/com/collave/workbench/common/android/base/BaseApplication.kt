package com.collave.workbench.common.android.base

import android.support.multidex.MultiDexApplication

/**
 * Created by Andrew on 6/2/2017.
 */
open class BaseApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
    }

}