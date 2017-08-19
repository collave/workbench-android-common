package com.collave.workbench.common.android.activity

import android.os.Bundle
import com.collave.workbench.common.android.base.BaseActivity
import com.collave.workbench.common.android.base.BaseUI
import org.jetbrains.anko.AnkoContext

/**
 * Created by Andrew on 6/7/2017.
 */
abstract class UIActivity : BaseActivity() {

    lateinit var ui: BaseUI
    abstract fun onCreateUI(): BaseUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = onCreateUI()
        ui.createView(AnkoContext.Companion.create(this, this, true))
        ui.fillView()
    }
}