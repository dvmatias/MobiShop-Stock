package com.cmdv.feature.ui.fragments.sales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.core.utils.logErrorMessage
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.SaleModel
import com.cmdv.feature.R
import kotlinx.android.synthetic.main.fragment_main_sales.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainSalesFragment : Fragment() {

    private val viewModel: MainSalesFragmentViewModel by viewModel()

    private lateinit var salesRecyclerAdapter: SaleRecyclerAdapter

    companion object {
        @JvmStatic
        fun newInstance() =
            MainSalesFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main_sales, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerSale()
        observeSales()
    }

    private fun setupRecyclerSale() {
        salesRecyclerAdapter = SaleRecyclerAdapter(requireContext())
        recyclerSale.apply {
            adapter = salesRecyclerAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun observeSales() {
        viewModel.sales.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                LiveDataStatusWrapper.Status.LOADING -> logErrorMessage("Loading")
                LiveDataStatusWrapper.Status.SUCCESS -> {
                    salesRecyclerAdapter.setItems(it.data as List<SaleModel>)
                }
                LiveDataStatusWrapper.Status.ERROR -> logErrorMessage("Error")
            }
        })
        viewModel.getSales()
    }
}