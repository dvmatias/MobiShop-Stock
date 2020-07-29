package com.cmdv.feature.ui.fragments.home.tabs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.components.TabFragmentPlaceHolder
import com.cmdv.core.Constants
import com.cmdv.core.navigator.Navigator
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.ProductModel
import com.cmdv.feature.R
import com.cmdv.feature.ui.adapters.ProductItemListener
import com.cmdv.feature.ui.adapters.RecyclerProductAdapter
import com.cmdv.feature.ui.decorations.ItemProductDecoration
import com.google.gson.Gson
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_main_tab_product_list.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class MainTabProductListFragment : TabFragmentPlaceHolder(), ProductItemListener {

    private val viewModel: MainTabProductListFragmentViewModel by viewModel()
    private val itemProductDecoration: ItemProductDecoration by inject()
    private val productAdapter: RecyclerProductAdapter by inject()
    private val gson: Gson by inject()
    private val navigator: Navigator by inject()

    private var listener: MainProductListFragmentListener? = null

    override fun newInstance(): Fragment =
        MainTabProductListFragment().apply {
            arguments = Bundle().apply {}
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_main_tab_product_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSwipeRefresh()
        setupRecyclerProduct()
        getProducts()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainProductListFragmentListener) {
            this.listener = context
        } else {
            throw IllegalStateException("Calling Activity mus implement")
        }
    }

    private fun setupSwipeRefresh() {
        swipeRefreshMain.setOnRefreshListener { onRefresh() }
    }

    private fun onRefresh() {
        viewModel.getProducts()
    }

    private fun setupRecyclerProduct() {
        recyclerProducts.apply {
            addItemDecoration(itemProductDecoration)
            layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = productAdapter
        }
    }

    private fun getProducts() {
        viewModel.products.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                LiveDataStatusWrapper.Status.LOADING -> setupLoadingScreen()
                LiveDataStatusWrapper.Status.SUCCESS -> setupSuccessScreen(it.data as ArrayList<ProductModel>)
                LiveDataStatusWrapper.Status.ERROR -> setupErrorScreen()
            }
        })
        viewModel.getProducts()
    }

    /**
     * Product list is being fetched.
     */
    private fun setupLoadingScreen() {
        swipeRefreshMain.apply {
            isEnabled = false
            isRefreshing = true
        }
        contentMain.visibility = View.GONE
    }

    /**
     * Product list fetching error.
     */
    private fun setupErrorScreen() {
        swipeRefreshMain.apply {
            isRefreshing = false
            isEnabled = true
        }
        contentMain.visibility = View.GONE
        noProductsFoundLayout.visibility = View.GONE
    }

    /**
     * Product list fetching success.
     */
    private fun setupSuccessScreen(products: ArrayList<ProductModel>) {
        swipeRefreshMain.apply {
            isRefreshing = false
            isEnabled = true
        }
        if (products.size > 0) {
            productAdapter.apply {
                setOverFlowMenuListener(this@MainTabProductListFragment)
                setProducts(products)
                // TODO
//                if (!this@MainActivity.query.isNullOrEmpty()) {
//                    filter.filter(this@MainActivity.query)
//                }
            }
            contentMain.visibility = View.VISIBLE
            noProductsFoundLayout.visibility = View.GONE
        } else {
            noProductsFoundLayout.visibility = View.VISIBLE
            contentMain.visibility = View.GONE
        }
    }

    /**
     * Triggered by an Add action on product overflow menu
     */
    private fun openAddProductDialog() {
        // TODO
    }

    /**
     * Triggered by an Edit action on product overflow menu
     */
    private fun goToEditProduct(position: Int) {
        val bundle = Bundle()
        bundle.putString(Constants.EXTRA_PRODUCT_KEY, gson.toJson(productAdapter.getProduct(position), ProductModel::class.java))
        navigator.toEditProductScreenForResult(
            activityOrigin = activity!!,
            bundle = bundle,
            options = null,
            requestCode = Constants.REQUEST_CODE_EDIT_PRODUCT,
            finish = false
        )
    }

    /**
     * Triggered by a Delete action on product overflow menu
     */
    private fun deleteProduct() {
        // TODO
    }

    /**
     * [ProductItemListener] implementation.
     */
    override fun onOverflowMenuButtonClick(position: Int, anchorView: View) {
        PopupMenu(activity!!, anchorView).apply {
            inflate(R.menu.product_popup_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.actionAddProduct -> openAddProductDialog()
                    R.id.actionEditProduct -> goToEditProduct(position)
                    R.id.actionDeleteProduct -> deleteProduct()

                }
                true
            }
            show()
        }
    }

    override fun onAddProductToShopCartButtonClick(position: Int) {
        listener?.onSwipeActionAddProductToShopCart(productAdapter.getProduct(position))
    }

    /**
     * Interface to be implemented by calling activity.
     * Communicates action in thi fragment to calling activity.
     */
    interface MainProductListFragmentListener {

        fun onSwipeActionAddProductToShopCart(product: ProductModel)

    }
}