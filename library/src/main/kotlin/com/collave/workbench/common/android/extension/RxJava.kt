package com.collave.workbench.common.android.extension

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.alert
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by Andrew on 5/8/2017.
 */
fun <T> Observable<T>.onIOforAndroidUI() : Observable<T> {
    return this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.onAndroidUI() : Observable<T> {
    return this
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.retryWithDelay(timeMillisProvider: (Throwable, Int)->Long?): Observable<T> {
    return this.retryWhen { error ->
        var count = 0
        error.flatMap { th ->
            val millis = timeMillisProvider.invoke(th, ++count) ?: return@flatMap Observable.error<T>(th)
            if (millis < 0) return@flatMap Observable.error<T>(th)
            Observable.timer(millis, TimeUnit.MILLISECONDS)
        }
    }
}

fun <T> Observable<T>.retryWithDelay(millis: Long, maxCount: Int = 0): Observable<T> {
    return retryWithDelay { _, count -> if (maxCount > 0 && count == maxCount) null else millis }
}


fun <T> Single<T>.retryWithDelay(timeMillisProvider: (Throwable, Int)->Long?): Single<T> {
    return this.retryWhen { error ->
        var count = 0
        error.flatMap { th ->
            val millis = timeMillisProvider.invoke(th, ++count) ?: return@flatMap Flowable.error<T>(th)
            if (millis < 0) return@flatMap Flowable.error<T>(th)
            Flowable.timer(millis, TimeUnit.MILLISECONDS)
        }
    }
}

fun <T> Single<T>.retryWithDelay(millis: Long, maxCount: Int = 0): Single<T> {
    return retryWithDelay { _, count -> if (maxCount > 0 && count == maxCount) null else millis }
}

fun <T> Single<T>.onIOforAndroidUI() : Single<T> {
    return this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.onAndroidUI() : Single<T> {
    return this
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.withProgressDialog(context: Context, title: String, message: String) : Single<T> {
    var progressDialog: ProgressDialog? = null
    return this
            .doOnSubscribe {
                if ((context as? Activity)?.isFinishing == true) return@doOnSubscribe
                progressDialog = ProgressDialog.show(context, title, message, true, false)
            }
            .doAfterTerminate {
                val dlg = progressDialog
                if (dlg != null && dlg.isShowing) {
                    dlg.dismiss()
                }
            }
}

fun <T> Single<T>.subscribeWithErrorDialog(context: Context, callback: (T?, Throwable?, Boolean)->Unit) : Disposable {
    return this.subscribe({ data ->
        callback(data, null, false)
    }) { error ->
        when (error) {
            is IOException -> {
                with (context) {
                    alert {
                        title = "Network Error"
                        message = "Your internet seems unstable. Please try the request again."
                        positiveButton("Retry") {
                            callback(null, error, true)
                        }
                        negativeButton("Cancel") { }
                    }.show()
                }
            }
            else -> {
                callback(null, error, false)
            }
        }
    }
}