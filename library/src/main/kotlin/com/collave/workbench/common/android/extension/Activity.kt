package com.collave.workbench.common.android.extension

import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity

/**
 * Created by Andrew on 6/2/2017.
 */
fun AppCompatActivity.fragmentManagerTransact(init: FragmentTransaction.()->Unit) {
    val tx = supportFragmentManager.beginTransaction()
    init(tx)
    tx.commit()
}