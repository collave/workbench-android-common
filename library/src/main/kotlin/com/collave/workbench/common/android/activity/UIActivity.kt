package com.collave.workbench.common.android.activity

import android.support.v4.app.Fragment
import com.collave.workbench.common.android.base.BaseUI

/**
 * Created by Andrew on 6/7/2017.
 */
abstract class UIActivity : FragmentContainerActivity() {

    abstract fun getUI(): BaseUI

    override fun getStartingFragment(): Fragment {
        return getUI().toFragment()
    }

}