package com.cmdv.feature.ui.fragments.home.tabs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.components.TabFragmentPlaceHolder
import com.cmdv.core.utils.logErrorMessage
import com.cmdv.domain.models.ProductModel
import com.cmdv.domain.models.ShopCartModel
import com.cmdv.feature.R
import kotlinx.android.synthetic.main.fragment_shop_cart_section.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainTabShopCartListFragment : TabFragmentPlaceHolder() {

    private val viewModel: MainTabShopCartFragmentViewModel by viewModel()

    private lateinit var shopCartAdapter: MainTabShopCartRecyclerAdapter

    private lateinit var shopCartListFragmentListener: MainTabShopCartListFragmentListener

    override fun newInstance(): Fragment =
        MainTabShopCartListFragment().apply {
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

        // TODO Delete: use this only to delete all shop carts in DB
//        viewModel.deleteAll()
        setupRecyclerShopCart()
        viewModel.liveDataOpenShopCarts.observe(this, Observer { list ->
            logErrorMessage("$list")
            shopCartAdapter.setItems(list)
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainTabShopCartListFragmentListener) {
            this.shopCartListFragmentListener = context
        } else {
            throw IllegalAccessException("Calling activity mus implement ${MainTabShopCartListFragmentListener::class.java.simpleName}")
        }
    }

    private fun setupRecyclerShopCart() {
        activity?.let {
            shopCartAdapter = MainTabShopCartRecyclerAdapter(it, shopCartListFragmentListener)
            recyclerViewShopCart.apply {
                addItemDecoration(ShopCartItemDecoration())
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                adapter = shopCartAdapter
            }
        }
    }

    /**
     * Interface to be implemented by calling activity.
     * Communicates action over this fragment to calling activity.
     */
    interface MainTabShopCartListFragmentListener {

        fun onEditShopCartProductClick()

        fun onDeleteShopCartProductClick()

        fun onCloseSaleClick(shopCart: ShopCartModel)

    }

}