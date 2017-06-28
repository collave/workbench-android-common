package com.collave.workbench.common.android.extension

/**
 * Created by Andrew on 6/9/2017.
 */
fun String.ifEmpty(default: String): String {
    return if (isEmpty()) default else this
}

fun String?.ifNullOrEmpty(default: String): String {
    return if (this == null || this.isEmpty()) default else this
}

fun String.ifNotEmpty(transform: (String.()->String)? = null): String {
    if (isNotEmpty()) {
        return transform?.invoke(this) ?: this
    }
    return ""
}