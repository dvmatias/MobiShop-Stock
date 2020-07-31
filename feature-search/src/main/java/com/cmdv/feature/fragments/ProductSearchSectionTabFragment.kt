package com.cmdv.feature.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.components.TabFragmentPlaceHolder
import com.cmdv.core.utils.logErrorMessage
import com.cmdv.domain.models.ProductModel
import com.cmdv.feature.R
import kotlinx.android.synthetic.main.fragment_product_search_section.*
import org.koin.android.ext.android.inject

class ProductSearchSectionTabFragment : TabFragmentPlaceHolder(), EventSearchProductObserver {

    private val productSearchAdapter: ProductSearchRecyclerAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_product_search_section, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerProduct()
        eventShowFiltersAndHistory()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as ObservableSearch).registerObserver(this)
    }

    override fun onDetach() {
        super.onDetach()
        (activity as ObservableSearch).removeObserver(this)
    }

    private fun setupRecyclerProduct() {
        recyclerProducts.apply {
            layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = productSearchAdapter
        }
    }

    private fun showFiltersAndHistoryScreen() {
        layoutFiltersAndSearchHistory.visibility = View.VISIBLE
        recyclerProducts.visibility = View.GONE
        layoutNotFound.visibility = View.GONE
        frameLoading.visibility = View.GONE
    }

    private fun showLoadingScreen() {
        layoutFiltersAndSearchHistory.visibility = View.GONE
        recyclerProducts.visibility = View.GONE
        layoutNotFound.visibility = View.GONE
        frameLoading.visibility = View.VISIBLE
    }

    private fun showFoundScreen(products: List<ProductModel>) {
        layoutFiltersAndSearchHistory.visibility = View.GONE
        recyclerProducts.visibility = View.VISIBLE
        layoutNotFound.visibility = View.GONE
        frameLoading.visibility = View.GONE

        val productList = arrayListOf<ProductModel>()
        products.toCollection(productList)
        productSearchAdapter.setProducts(productList)
    }

    private fun showNotFoundScreen() {
        layoutFiltersAndSearchHistory.visibility = View.GONE
        recyclerProducts.visibility = View.GONE
        layoutNotFound.visibility = View.VISIBLE
        frameLoading.visibility = View.GONE
    }

    private fun showErrorScreen() {
        // TODO
    }

    /**
     * [EventSearchProductObserver] implementation.
     */
    override fun eventShowFiltersAndHistory() {
        showFiltersAndHistoryScreen()
    }

    override fun eventOnSearchProductsLoading() {
        showLoadingScreen()
    }

    override fun eventOnSearchProductsSuccess(products: List<ProductModel>?) {
        products?.let {
            if (products.isEmpty()) {
                showNotFoundScreen()
            } else {
                showFoundScreen(products)
            }
        } ?: showErrorScreen()
    }

    override fun eventOnSearchProductsError() {
        logErrorMessage("eventOnSearchProductsError()")
        frameLoading.visibility = View.GONE
        recyclerProducts.visibility = View.GONE
        layoutFiltersAndSearchHistory.visibility = View.GONE
    }

    override fun newInstance(): Fragment =
        ProductSearchSectionTabFragment().apply {
            arguments = Bundle().apply {}
        }

}