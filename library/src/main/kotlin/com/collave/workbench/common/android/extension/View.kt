package com.collave.workbench.common.android.extension

import android.app.AlertDialog
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorCompat
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import org.jetbrains.anko.backgroundResource

/**
 * Created by Andrew on 5/10/2017.
 */

fun <T: ViewGroup, X: View> T.add(view: X, init: X.()->Unit): X {
    init(view)
    addView(view)
    return view
}

fun <T: ViewGroup, X: View> T.add(index: Int, view: X, init: X.()->Unit): X {
    init(view)
    addView(view, index)
    return view
}

fun <T: View> T.onLayoutReady(callback: T.()->Unit) {
    if (ViewCompat.isLaidOut(this)) {
        callback(this)
    } else {
        this.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                this@onLayoutReady.viewTreeObserver.removeOnGlobalLayoutListener(this)
                callback(this@onLayoutReady)
            }
        })
    }
}

fun View.animateCompat(): ViewPropertyAnimatorCompat {
    return ViewCompat.animate(this)
}

fun View.useSelectableBackground() {
    val outValue = TypedValue()
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
    backgroundResource = outValue.resourceId
}

fun AlertDialog?.dismissSafe() {
    if (this != null && isShowing) {
        dismiss()
    }
}