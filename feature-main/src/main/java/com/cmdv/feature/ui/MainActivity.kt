package com.cmdv.feature.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.core.navigator.Navigator
import com.cmdv.core.utils.logErrorMessage
import com.cmdv.domain.models.ProductModel
import com.cmdv.domain.models.Status
import com.cmdv.feature.R
import com.cmdv.feature.ui.adapters.RecyclerProductAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModel()

    private val navigator: Navigator by inject()

    private val productAdapter: RecyclerProductAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        setupCreateProductButton()
        setupRecyclerProduct()
    }

    private fun setupCreateProductButton() {
        fab.setOnClickListener {
            navigator.toAddProductScreen(
                this,
                null,
                null,
                false
            )
        }
    }

    private fun setupRecyclerProduct() {
        recyclerProducts.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = productAdapter
        }
        viewModel.products.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    logErrorMessage("Loading")
                }
                Status.SUCCESS -> {
                    logErrorMessage("Success")
                    productAdapter.setData(it.data as ArrayList<ProductModel>)
                }
                Status.ERROR -> {
                    logErrorMessage("Error")
                }
            }
        })
        viewModel.getProducts()
    }
}
