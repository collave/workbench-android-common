package com.collave.workbench.common.android

import android.os.Build
import android.view.View
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by Andrew on 6/2/2017.
 */
object CommonUtil {

    private val viewIdGenerator = AtomicInteger(15000000)

    fun generateViewId(): Int {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return generateUniqueViewId()
        } else {
            return View.generateViewId()
        }
    }

    private fun generateUniqueViewId(): Int {
        while (true) {
            val result = viewIdGenerator.get()
            var newValue = result + 1
            if (newValue > 0x00FFFFFF) newValue = 1
            if (viewIdGenerator.compareAndSet(result, newValue)) {
                return result
            }
        }
    }

}