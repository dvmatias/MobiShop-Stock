package com.cmdv.feature.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cmdv.components.dialog.createshopcart.ComponentCreateShopCartDialog
import com.cmdv.components.dialog.createshopcart.CreateShopCartDialogListener
import com.cmdv.feature.R
import com.cmdv.feature.ui.adapters.SectionsPagerAdapter
import kotlinx.android.synthetic.main.fragment_sales_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainSalesFragment : Fragment() {

    private val viewModel: MainSalesFragmentViewModel by viewModel()

    private val createShopCartDialogListener = object: CreateShopCartDialogListener {
        override fun onCreateShopCartDialogPositiveClick(name: String) {
            viewModel.createShopCart(name)
        }
    }

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

        fabCreateShopCart.setOnClickListener {
            openCreateShopCartDialog()
        }
    }

    private fun openCreateShopCartDialog() {
        activity?.let {
            ComponentCreateShopCartDialog(it, createShopCartDialogListener).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MainSalesFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}