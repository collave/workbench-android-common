package com.collave.workbench.common.android.component

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by Andrew on 6/3/2017.
 */
class SimpleFragmentPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    class Page(val title: String, val onCreateFragment: ()->Fragment)

    var pages = mutableListOf<Page>()

    fun add(title: String, init: ()->Fragment): SimpleFragmentPagerAdapter {
        pages.add(Page(title, init))
        return this
    }

    override fun getCount() = pages.size
    override fun getItem(position: Int) = pages[position].onCreateFragment()
    override fun getPageTitle(position: Int) = pages[position].title

}