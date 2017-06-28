package com.collave.workbench.common.android.component.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.collave.workbench.common.android.base.BaseUI
import org.jetbrains.anko.AnkoContext

/**
 * Created by Andrew on 5/11/2017.
 */
abstract class ListRecyclerViewUIAdapter : RecyclerView.Adapter<ListRecyclerViewUIAdapter.ViewHolder>() {

    class ViewHolder(context: Context, owner: Any, val ui: BaseUI):
            RecyclerView.ViewHolder(ui.createView(AnkoContext.create(context, owner)))

    abstract val list: List<*>
    override fun getItemCount() = list.size

    abstract fun onCreateUI(viewType: Int): BaseUI

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.context, parent, onCreateUI(viewType))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ui.data = list[position]
        holder.ui.fillView()
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.ui.recycleView()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.ui.destroyView()
    }

}