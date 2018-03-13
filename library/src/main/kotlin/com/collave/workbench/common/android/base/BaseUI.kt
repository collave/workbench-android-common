package com.collave.workbench.common.android.base

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Handler
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import com.collave.workbench.common.android.extension.onAndroidUI
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import java.lang.ref.WeakReference

/**
 * Created by Andrew on 5/10/2017.
 */
abstract class BaseUI(open var data: Any? = null) : AnkoComponent<Any> {

    protected val children = mutableListOf<BaseUI>()

    private var parentRef: WeakReference<BaseUI>? = null
    var parent: BaseUI?
        get() = parentRef?.get()
        set(value) {
            if (value != null)
                parentRef = WeakReference(value)
            else
                parentRef = null
        }

    lateinit var view: View
    lateinit var context: Context
    lateinit var ankoContext: AnkoContext<Any>

    var disposables: CompositeDisposable? = null

    class ActivityResult(val requestCode: Int, val resultCode: Int, val data: Intent?)
    private val onActivityResultSubject = PublishSubject.create<ActivityResult>()
    val onActivityResult = onActivityResultSubject.onAndroidUI()

    val handler by lazy { Handler() }

    fun Disposable.register() {
        disposables?.add(this)
    }

    val fragment get() = ankoContext.owner as? Fragment
    val activity get() = context as AppCompatActivity

    val fragmentManager: FragmentManager get() {
        val owner = ankoContext.owner
        return when (owner) {
            is Fragment -> owner.childFragmentManager
            is AppCompatActivity -> owner.supportFragmentManager
            else -> throw Exception()
        }
    }

    fun closeView() {
        val fragment = fragment
        if (fragment is DialogFragment) {
            fragment.dismiss()
            return
        }
        activity.finish()
    }

    override fun createView(ui: AnkoContext<Any>): View {
        context = ui.ctx
        ankoContext = ui
        disposables = CompositeDisposable()
        view = buildView(ui)
        return view
    }

    abstract fun buildView(ui: AnkoContext<Any>): View

    open fun fillView() {
        children.forEach { it.fillView() }
    }

    open fun recycleView() {
        children.forEach { it.recycleView() }
        disposables?.dispose()
        disposables = null
    }

    open fun destroyView() {
        children.forEach { it.destroyView() }
        children.clear()
        disposables?.dispose()
        disposables = null
    }

    open fun refreshView() {
        children.forEach { it.refreshView() }
    }

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        onActivityResultSubject.onNext(ActivityResult(requestCode, resultCode, data))
        children.asSequence().distinct().forEach { it.onActivityResult(requestCode, resultCode, data) }
    }

    fun AnkoContext<Any>.colorRes(resId: Int): Int {
        return ContextCompat.getColor(ctx, resId)
    }

    fun AnkoContext<Any>.color(hex: String): Int {
        return Color.parseColor(hex)
    }

    fun AnkoContext<Any>.vectorDrawable(resId: Int, theme: Resources.Theme? = null): VectorDrawableCompat {
        return VectorDrawableCompat.create(ctx.resources, resId, theme)!!
    }

    fun <T: BaseUI> ViewGroup.add(ankoContext: AnkoContext<Any>, ui: T, addAsChild: Boolean = true): T {
        if (addAsChild) {
            children.add(ui)
            ui.parent = this@BaseUI
        }

        val ctx = AnkoContext.Companion.create(ankoContext.ctx, ankoContext.owner)
        val view = ui.createView(ctx)
        addView(view)
        return ui
    }

}