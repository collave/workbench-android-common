package com.collave.workbench.common.android.component.ui

import android.support.annotation.IdRes
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import com.collave.workbench.common.android.CommonUtil
import com.collave.workbench.common.android.base.BaseUI
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.viewPager
import org.jetbrains.anko.verticalLayout

/**
 * Created by Andrew on 6/2/2017.
 */
abstract class TabbedViewPagerUI : BaseUI() {

    companion object {
        @IdRes val viewPagerId = CommonUtil.generateViewId()
    }

    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager

    override fun buildView(ui: AnkoContext<Any>) = with (ui) {
        verticalLayout {
            tabLayout = tabLayout {

            }
            viewPager = viewPager {
                id = viewPagerId
                adapter = createViewPagerAdapter()
            }
            tabLayout.setupWithViewPager(viewPager)
        }
    }

    abstract fun createViewPagerAdapter(): PagerAdapter

}