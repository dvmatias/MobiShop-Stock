package com.cmdv.feature.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerMainFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var fragmentList: List<Fragment> = listOf()

    fun setData(fragmentList: List<Fragment>) {
        if (fragmentList.isEmpty()) return
        this.fragmentList = fragmentList
        notifyDataSetChanged()
    }

    /**
     * Return the number of views available.
     */
    override fun getCount(): Int =
        fragmentList.size

    /**
     * Return the Fragment associated with a specified position.
     */
    override fun getItem(position: Int): Fragment =
        fragmentList[position]

}
