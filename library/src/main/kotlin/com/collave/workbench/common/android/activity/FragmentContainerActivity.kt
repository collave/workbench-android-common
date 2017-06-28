package com.collave.workbench.common.android.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.collave.workbench.common.android.base.BaseActivity
import com.collave.workbench.common.android.extension.fragmentManagerTransact

/**
 * Created by Andrew on 6/2/2017.
 */
abstract class FragmentContainerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentManagerTransact {
            add(android.R.id.content, getStartingFragment())
        }
    }

    abstract fun getStartingFragment(): Fragment

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragments = supportFragmentManager.fragments ?: return
        fragments
                .asSequence()
                .filterNotNull()
                .distinct()
                .filter { it.isResumed }
                .forEach { it.onActivityResult(requestCode, resultCode, data) }
    }
}