package com.cmdv.feature.ui.fragments.salestabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.cmdv.components.TabFragmentPlaceHolder
import com.cmdv.core.utils.logErrorMessage
import com.cmdv.feature.R
import org.koin.android.viewmodel.ext.android.viewModel

class ShopCartSectionTabFragment : TabFragmentPlaceHolder() {

    private val viewModel: ShopCartSectionTabFragmentViewModel by viewModel()

    override fun newInstance(): Fragment =
        ShopCartSectionTabFragment().apply {
            arguments = Bundle().apply {}
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_shop_cart_section, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.test.observe(this, Observer {
            // TODO manage shop cart list
            logErrorMessage("$it")
        })
    }

}