package com.cmdv.feature.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.cmdv.feature.R
import com.cmdv.feature.fragments.ProductsSearchSectionTabFragment
import com.cmdv.feature.fragments.SalesSearchSectionTabFragment

private val TABS = arrayOf(
    ProductsSearchSectionTabFragment() to R.string.labelTabProducts,
    SalesSearchSectionTabFragment() to R.string.labelTabSales
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