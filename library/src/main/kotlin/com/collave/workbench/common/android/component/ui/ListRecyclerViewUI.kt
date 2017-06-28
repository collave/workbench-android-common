package com.collave.workbench.common.android.component.ui

import android.support.v7.widget.LinearLayoutManager
import com.collave.workbench.common.android.base.BaseUI
import com.collave.workbench.common.android.component.state.StatefulDataRequest

/**
 * Created by Andrew on 6/5/2017.
 */
abstract class ListRecyclerViewUI<T: Any> : RecyclerViewUI() {

    val currentList get() = onSelectDataList(dataRequest.data)
    abstract val dataRequest: StatefulDataRequest<T>
    open val refreshArgs: Array<out Any?> get() = emptyArray()

    abstract fun onCreateItemUI(): BaseUI
    abstract fun onSelectDataList(data: T?): List<*>
    override fun createLayoutManager() = LinearLayoutManager(context)

    override val adapter = object : ListRecyclerViewUIAdapter() {
        override val list get() = currentList
        override fun onCreateUI(viewType: Int) = onCreateItemUI()
    }

    override fun fillView() {
        super.fillView()

        swipeRefreshLayout.setOnRefreshListener {
            dataRequest.execute(*refreshArgs)
            swipeRefreshLayout.isRefreshing = false
        }

        with (dataRequest) {

            onDataUpdated
                    .subscribe { adapter.notifyDataSetChanged() }
                    .register()

            onStateUpdated
                    .subscribe {
                        swipeRefreshLayout.isRefreshing = it.isInProgress && data != null
                        setErrorBarVisible(it.isRequestError)

                        val data = data
                        if (it.isInProgress && data == null) {
                            showView(RecyclerViewUI.ViewTag.Loading)
                        } else if (it.isRequestError && data == null) {
                            showView(RecyclerViewUI.ViewTag.Error)
                        } else if (it.isSuccessful && data != null && currentList.isEmpty()) {
                            showView(RecyclerViewUI.ViewTag.Empty)
                        } else {
                            showView(RecyclerViewUI.ViewTag.List)
                        }
                    }
                    .register()

            executeInitial(*refreshArgs)
        }
    }

}