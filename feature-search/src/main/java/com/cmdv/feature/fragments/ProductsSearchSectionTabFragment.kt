package com.cmdv.feature.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cmdv.feature.R
import com.cmdv.components.TabFragmentPlaceHolder
import com.cmdv.core.utils.logErrorMessage
import com.cmdv.domain.models.ProductModel
import kotlinx.android.synthetic.main.fragment_products_search_section.*

class ProductsSearchSectionTabFragment : TabFragmentPlaceHolder(), EventSearchProductObserver {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_products_search_section, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    private fun setupNotFoundScreen() {
        frameLoading.visibility = View.GONE
        layoutFiltersAndSearchHistory.visibility = View.GONE
    }

    private fun setupErrorScreen() {

    }

    /**
     * [EventSearchProductObserver] implementation.
     */
    override fun eventShowFiltersAndHistory() {
        frameLoading.visibility = View.GONE
        layoutFiltersAndSearchHistory.visibility = View.VISIBLE
    }

    override fun eventOnSearchProductsLoading() {
        logErrorMessage("eventOnSearchProductsLoading()")
        frameLoading.visibility = View.VISIBLE
        layoutFiltersAndSearchHistory.visibility = View.GONE
    }

    override fun eventOnSearchProductsSuccess(products: List<ProductModel>?) {
        logErrorMessage("eventOnSearchProductsSuccess()")
        frameLoading.visibility = View.GONE
        layoutFiltersAndSearchHistory.visibility = View.GONE

        products?.let {
            if (products.isEmpty()) {
                setupNotFoundScreen()
            } else {
                // TODO populate recycler with products
            }
        } ?: setupErrorScreen()
    }

    override fun eventOnSearchProductsError() {
        logErrorMessage("eventOnSearchProductsError()")
        frameLoading.visibility = View.GONE
        layoutFiltersAndSearchHistory.visibility = View.GONE
    }

    override fun newInstance(): Fragment =
        ProductsSearchSectionTabFragment().apply {
            arguments = Bundle().apply {}
        }

}