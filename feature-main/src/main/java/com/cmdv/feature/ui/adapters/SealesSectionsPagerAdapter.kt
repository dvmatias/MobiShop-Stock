package com.cmdv.feature.ui.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.cmdv.feature.R
import com.cmdv.feature.ui.fragments.salestabs.SalesSectionTabFragment
import com.cmdv.feature.ui.fragments.salestabs.ShopCartSectionTabFragment

private val TABS = arrayOf(
    SalesSectionTabFragment() to R.string.labelTabSales,
    ShopCartSectionTabFragment() to R.string.labelTabShopCart
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