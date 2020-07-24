package com.cmdv.feature.ui.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.cmdv.feature.R
import com.cmdv.feature.ui.fragments.home.tabs.MainTabProductListFragment
import com.cmdv.feature.ui.fragments.home.tabs.MainTabShopCartFragment

private val TABS = arrayOf(
    MainTabProductListFragment() to R.string.label_tab_products,
    MainTabShopCartFragment() to R.string.label_tab_shop_cart
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment =
        TABS[position].first.newInstance()


    override fun getPageTitle(position: Int): CharSequence? =
        context.resources.getString(TABS[position].second)

    override fun getCount(): Int =
        TABS.size

}