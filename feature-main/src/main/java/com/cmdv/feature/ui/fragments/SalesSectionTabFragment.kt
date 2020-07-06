package com.cmdv.feature.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cmdv.components.TabFragmentPlaceHolder
import com.cmdv.feature.R

class SalesSectionTabFragment : TabFragmentPlaceHolder() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sales_section, container, false)
    }

    override fun newInstance(): Fragment =
        SalesSectionTabFragment().apply {
            arguments = Bundle().apply {}
        }

}