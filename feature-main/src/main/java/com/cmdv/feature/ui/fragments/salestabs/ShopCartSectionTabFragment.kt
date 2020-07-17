package com.cmdv.feature.ui.fragments.salestabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cmdv.components.TabFragmentPlaceHolder
import com.cmdv.feature.R

class ShopCartSectionTabFragment : TabFragmentPlaceHolder() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_shop_cart_section, container, false)

    override fun newInstance(): Fragment =
        ShopCartSectionTabFragment().apply {
            arguments = Bundle().apply {}
        }

}