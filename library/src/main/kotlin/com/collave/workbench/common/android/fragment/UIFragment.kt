package com.collave.workbench.common.android.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.collave.workbench.common.android.base.BaseFragment
import com.collave.workbench.common.android.base.BaseUI
import org.jetbrains.anko.AnkoContext

/**
 * Created by Andrew on 6/2/2017.
 */
abstract class UIFragment : BaseFragment() {

    lateinit var ui: BaseUI
    abstract fun onCreateUI(): BaseUI

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ui = onCreateUI()
        return ui.createView(AnkoContext.Companion.create(context!!, this))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ui.fillView()
    }

    override fun onDestroyView() {
        val view = view
        if (view != null)
            ui.destroyView()
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ui.onActivityResult(requestCode, resultCode, data)
    }

}