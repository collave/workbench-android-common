package com.collave.workbench.common.android.component.ui

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ViewFlipper
import com.collave.workbench.common.android.base.BaseUI
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.swipeRefreshLayout

/**
 * Created by Andrew on 5/11/2017.
 */
abstract class RecyclerViewUI : BaseUI() {

    enum class ViewTag(val index: Int) { List(0), Empty(1), Loading(2), Error(3) }

    abstract fun createLayoutManager(): RecyclerView.LayoutManager
    abstract val adapter: RecyclerView.Adapter<*>
    lateinit var verticalLayout: LinearLayout
    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var viewFlipper: ViewFlipper

    lateinit var emptyView: View
    lateinit var loadingView: View
    lateinit var errorView: View
    lateinit var errorBarView: View

    open var refreshAllowed = true
        set(value) {
            field = value
            swipeRefreshLayout.isEnabled = value
        }

    override fun buildView(ui: AnkoContext<Any>) = with (ui) {
        verticalLayout = verticalLayout {
            verticalLayout {
                errorBarView = createErrorBarView(this).lparams(matchParent, wrapContent)
                swipeRefreshLayout = swipeRefreshLayout {
                    isEnabled = refreshAllowed
                    viewFlipper = viewFlipper {
                        recyclerView = createRecyclerView().apply {
                            layoutManager = this@RecyclerViewUI.createLayoutManager()
                            adapter = this@RecyclerViewUI.adapter
                        }
                        emptyView = createEmptyView(this)
                        loadingView = createLoadingView(this)
                        errorView = createErrorView(this)
                        displayedChild = 0
                    }
                }.lparams(matchParent, 0, 1f)
            }.lparams(matchParent, 0, 1f)
        }
        verticalLayout
    }

    open fun createRecyclerView(): RecyclerView = RecyclerView(context)
    open fun createErrorBarView(ui: _LinearLayout): View = ui.frameLayout()
    open fun createEmptyView(ui: @AnkoViewDslMarker ViewFlipper): View = ui.frameLayout()
    open fun createErrorView(ui: @AnkoViewDslMarker ViewFlipper): View = ui.frameLayout()
    open fun createLoadingView(ui: @AnkoViewDslMarker ViewFlipper): View = with (ui) {
        frameLayout {
            progressBar().lparams {
                gravity = Gravity.CENTER
            }
        }
    }

    fun showView(tag: ViewTag) {
        viewFlipper.displayedChild = tag.index
    }

    fun setErrorBarVisible(visible: Boolean) {
        errorBarView.visibility = if (visible) View.VISIBLE else View.GONE
    }

}