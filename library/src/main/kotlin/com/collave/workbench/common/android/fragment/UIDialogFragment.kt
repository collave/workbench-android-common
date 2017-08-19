package com.collave.workbench.common.android.fragment

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.view.ViewGroup
import com.collave.workbench.common.android.base.BaseUI
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

/**
 * Created by Andrew on 6/7/2017.
 */
open class UIDialogFragment : DialogFragment() {

    lateinit var ui: BaseUI
    var isFullHeight = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity)
        val view = ui.createView(AnkoContext.Companion.create(context, this))
        view.layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
        onViewCreated(view, savedInstanceState)
        dialog.setContentView(view)
        dialog.window.setLayout(matchParent, if (isFullHeight) matchParent else wrapContent)
        return dialog
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ui.fillView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ui.destroyView()
    }

}