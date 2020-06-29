package com.cmdv.feature.ui

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.core.Constants.Companion.EXTRA_PRODUCT_KEY
import com.cmdv.core.Constants.Companion.REQUEST_CODE_EDIT_PRODUCT
import com.cmdv.core.navigator.Navigator
import com.cmdv.core.utils.logErrorMessage
import com.cmdv.domain.models.ProductModel
import com.cmdv.domain.models.Status
import com.cmdv.feature.R
import com.cmdv.feature.ui.adapters.RecyclerProductAdapter
import com.cmdv.feature.ui.controllers.SwipeToAddToCartOrEditCallback
import com.cmdv.feature.ui.controllers.SwipeToAddToCartOrEditCallback.SwipeActionListener
import com.cmdv.feature.ui.decorations.ItemProductDecoration
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModel()

    private val navigator: Navigator by inject()

    private val productAdapter: RecyclerProductAdapter by inject()

    private val itemProductDecoration: ItemProductDecoration by inject()

    private val gson: Gson by inject()

    private lateinit var searchView: SearchView

    private var query: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()
        setupSwipeRefresh()
        setupRecyclerProduct()
        getProducts()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchItem: MenuItem = menu!!.findItem(R.id.actionSearch)
        @Suppress("DEPRECATION")
        searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.setOnCloseListener { true }
        searchView.maxWidth = Integer.MAX_VALUE

        val searchPlate: EditText =
            searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        searchPlate.hint = "Search"
        val searchPlateView: View =
            searchView.findViewById(androidx.appcompat.R.id.search_plate)
        searchPlateView.setBackgroundColor(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
        )

        val closeButton: ImageView = searchView.findViewById(R.id.search_close_btn) as ImageView
        closeButton.setOnClickListener {
            if (searchPlate.text.isNotEmpty()) {
                searchPlate.setText("")
                searchView.setQuery("", false)
                searchItem.collapseActionView()
            } else {
                onBackPressed()
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                this@MainActivity.query = query
                productAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                this@MainActivity.query = newText
                productAdapter.filter.filter(newText)
                return false
            }
        })

        val searchManager: SearchManager =
            getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionCreateProduct -> {
                navigator.toAddProductScreen(this, null, null, false)
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.onActionViewCollapsed()
        } else {
            super.onBackPressed()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.title = null
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupSwipeRefresh() {
        swipeRefreshMain.setOnRefreshListener { onRefresh() }
    }

    private fun onRefresh() {
        logErrorMessage("onRefresh()")
        viewModel.getProducts()
    }

    private fun setupRecyclerProduct() {
        recyclerProducts.apply {
            addItemDecoration(itemProductDecoration)
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = productAdapter
        }
        val itemTouchHelper = ItemTouchHelper(SwipeToAddToCartOrEditCallback(this, swipeActionListener))
        itemTouchHelper.attachToRecyclerView(recyclerProducts)
    }

    private fun getProducts() {
        viewModel.products.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    logErrorMessage("Loading")
                    setupLoadingScreen()
                }
                Status.SUCCESS -> {
                    logErrorMessage("Success")
                    setupSuccessScreen(it.data as ArrayList<ProductModel>)
                }
                Status.ERROR -> {
                    logErrorMessage("Error")
                    setupErrorScreen()
                }
            }
        })
        viewModel.getProducts()
    }

    /**
     *
     */
    private fun setupLoadingScreen() {
        swipeRefreshMain.apply {
            isRefreshing = true
            isEnabled = false
        }
        contentMain.visibility = View.GONE
    }

    /**
     *
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
     *
     */
    private fun setupSuccessScreen(products: ArrayList<ProductModel>) {
        swipeRefreshMain.apply {
            isRefreshing = false
            isEnabled = true
        }
        if (products.size > 0) {
            productAdapter.apply {
                setProducts(products)
                if (!this@MainActivity.query.isNullOrEmpty()) {
                    filter.filter(this@MainActivity.query)
                }
            }
            contentMain.visibility = View.VISIBLE
            noProductsFoundLayout.visibility = View.GONE
        } else {
            noProductsFoundLayout.visibility = View.VISIBLE
            contentMain.visibility = View.GONE
        }
    }

    /**
     * [SwipeActionListener] implementation.
     */
    private val swipeActionListener = object : SwipeActionListener {
        override fun onActionEdit(position: Int) {
            val bundle = Bundle()
            bundle.putString(
                EXTRA_PRODUCT_KEY,
                gson.toJson(productAdapter.getProduct(position), ProductModel::class.java)
            )
            navigator.toEditProductScreenForResult(
                activityOrigin = this@MainActivity,
                bundle = bundle,
                options = null,
                requestCode = REQUEST_CODE_EDIT_PRODUCT,
                finish = false
            )
        }

        override fun onActionAddToCart(position: Int) {
            Snackbar.make(window.decorView.rootView, "Action Ad To Cart detected on product position $position", Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     *
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_EDIT_PRODUCT -> {
                if (Activity.RESULT_OK == resultCode) {
                    Toast.makeText(this, "EditProductActivity RESULT_OK", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "EditProductActivity RESULT_KO", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}