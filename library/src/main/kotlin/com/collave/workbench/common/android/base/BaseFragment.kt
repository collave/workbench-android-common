package com.collave.workbench.common.android.base

import android.content.Intent
import android.support.v4.app.Fragment

/**
 * Created by Andrew on 6/2/2017.
 */
open class BaseFragment : Fragment() {

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragments = childFragmentManager.fragments ?: return
        fragments
                .asSequence()
                .distinct()
                .filter { it.isResumed }
                .forEach { it.onActivityResult(requestCode, resultCode, data) }
    }

}