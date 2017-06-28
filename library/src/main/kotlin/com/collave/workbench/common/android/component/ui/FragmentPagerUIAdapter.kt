package com.collave.workbench.common.android.component.ui

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.collave.workbench.common.android.base.BaseUI

/**
 * Created by Andrew on 6/3/2017.
 */
class FragmentPagerUIAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    class Page(val title: String, val createUI: ()->BaseUI)

    var pages = mutableListOf<Page>()

    fun add(title: String, init: ()->BaseUI): FragmentPagerUIAdapter {
        pages.add(Page(title, init))
        return this
    }

    override fun getCount() = pages.size
    override fun getItem(position: Int) = pages[position].createUI().toFragment()
    override fun getPageTitle(position: Int) = pages[position].title

}