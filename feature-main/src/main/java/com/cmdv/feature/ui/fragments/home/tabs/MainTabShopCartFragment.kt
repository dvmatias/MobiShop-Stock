package com.cmdv.feature.ui.fragments.home.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.components.TabFragmentPlaceHolder
import com.cmdv.core.utils.logErrorMessage
import com.cmdv.feature.R
import kotlinx.android.synthetic.main.fragment_shop_cart_section.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainTabShopCartFragment : TabFragmentPlaceHolder() {

    private val viewModel: MainTabShopCartFragmentViewModel by viewModel()

    private lateinit var shopCartAdapter: MainTabShopCartRecyclerAdapter

    override fun newInstance(): Fragment =
        MainTabShopCartFragment().apply {
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
//         viewModel.deleteAll()
        setupRecyclerShopCart()
        viewModel.liveDataOpenShopCarts.observe(this, Observer { list ->
            logErrorMessage("$list")
            shopCartAdapter.setItems(list)
        })
    }

    private fun setupRecyclerShopCart() {
        shopCartAdapter = MainTabShopCartRecyclerAdapter(activity!!)
        recyclerViewShopCart.apply {
            addItemDecoration(ShopCartItemDecoration())
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = shopCartAdapter
        }
    }

}