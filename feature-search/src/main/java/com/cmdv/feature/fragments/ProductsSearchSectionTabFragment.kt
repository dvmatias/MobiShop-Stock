package com.cmdv.feature.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cmdv.feature.R
import com.cmdv.components.TabFragmentPlaceHolder

class ProductsSearchSectionTabFragment : TabFragmentPlaceHolder() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products_search_section, container, false)
    }

    override fun newInstance(): Fragment =
        ProductsSearchSectionTabFragment().apply {
            arguments = Bundle().apply {}
        }

}