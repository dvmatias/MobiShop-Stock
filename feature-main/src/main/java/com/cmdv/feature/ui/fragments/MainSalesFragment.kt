package com.cmdv.feature.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cmdv.feature.R
import com.cmdv.feature.ui.adapters.SectionsPagerAdapter
import kotlinx.android.synthetic.main.fragment_sales_main.*

class MainSalesFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_sales_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sectionsPagerAdapter = SectionsPagerAdapter(requireContext(), activity!!.supportFragmentManager)
        viewPagerSales.adapter = sectionsPagerAdapter
        tabsSales.setupWithViewPager(viewPagerSales)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MainSalesFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}