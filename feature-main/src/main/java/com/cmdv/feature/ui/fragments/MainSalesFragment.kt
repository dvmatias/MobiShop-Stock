package com.cmdv.feature.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cmdv.feature.R
import com.cmdv.feature.ui.adapters.SectionsPagerAdapter
import kotlinx.android.synthetic.main.fragment_sales_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainSalesFragment : Fragment() {

    private val viewModel: MainSalesFragmentViewModel by viewModel()

    private var listener: MainSalesFragmentListener? = null

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
            this.listener?.onCreateShopCartClick()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainSalesFragmentListener) {
            this.listener = context
        } else {
            throw IllegalStateException("Calling Activity mus implement")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MainSalesFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    /**
     * Interface to be implemented by calling activity.
     * Communicates action in thi fragment to calling activity.
     */
    interface MainSalesFragmentListener {

        fun onCreateShopCartClick()

    }
}