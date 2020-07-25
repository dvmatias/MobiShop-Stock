package com.cmdv.feature.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import com.cmdv.components.bottomnavmain.ComponentBottomNav
import com.cmdv.components.dialog.addproducttoshopcart.AddProductToShopCartDialogListener
import com.cmdv.components.dialog.addproducttoshopcart.ComponentAddProductToShopCartDialog
import com.cmdv.components.dialog.createshopcart.ComponentCreateShopCartDialog
import com.cmdv.components.dialog.createshopcart.CreateShopCartDialogListener
import com.cmdv.core.Constants.Companion.REQUEST_CODE_EDIT_PRODUCT
import com.cmdv.core.navigator.Navigator
import com.cmdv.core.utils.logErrorMessage
import com.cmdv.domain.models.ItemMainPageModel
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.ProductModel
import com.cmdv.domain.models.ShopCartModel
import com.cmdv.feature.R
import com.cmdv.feature.ui.adapters.PagerMainFragmentAdapter
import com.cmdv.feature.ui.fragments.home.MainHomeFragment
import com.cmdv.feature.ui.fragments.home.MainHomeFragmentListener
import com.cmdv.feature.ui.fragments.home.tabs.MainTabProductListFragment
import com.cmdv.feature.ui.fragments.home.tabs.MainTabProductListFragment.MainProductListFragmentListener
import com.cmdv.feature.ui.fragments.home.tabs.MainTabShopCartListFragment.MainTabShopCartListFragmentListener
import com.cmdv.feature.ui.fragments.profile.MainProfileFragment
import com.cmdv.feature.ui.fragments.sales.MainSalesFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(),
    MainHomeFragmentListener,
    MainProductListFragmentListener,
    MainTabShopCartListFragmentListener {

    private val viewModel: MainActivityViewModel by viewModel()

    private val navigator: Navigator by inject()

    private lateinit var searchView: SearchView

    private var query: String? = null

    private val itemMainPageList: MutableList<ItemMainPageModel> = mutableListOf(
        ItemMainPageModel(
            R.string.labelBottomNavTabHome,
            "home",
            R.drawable.ic_bottom_nav_home_32dp,
            R.drawable.ic_bottom_nav_home_selected_32dp,
            MainHomeFragment.newInstance(),
            true
        ),
        ItemMainPageModel(
            R.string.labelBottomNavTabSales,
            "shop_cart",
            R.drawable.ic_bottom_nav_shop_cart_32dp,
            R.drawable.ic_bottom_nav_shop_cart_selected_32dp,
            MainSalesFragment.newInstance(),
            true
        ),
        ItemMainPageModel(
            R.string.labelBottomNavTabProfile,
            "profile",
            R.drawable.ic_bottom_nav_profile_32dp,
            R.drawable.ic_bottom_nav_profile_selected_32dp,
            MainProfileFragment.newInstance(),
            true
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()
        setupPager()
        setupBottomNav()

        // TODO temp
        imageViewLogo.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
//        val searchItem: MenuItem = menu!!.findItem(R.id.actionSearch)
//        @Suppress("DEPRECATION")
//        searchView = MenuItemCompat.getActionView(searchItem) as SearchView
//        searchView.setOnCloseListener { true }
//        searchView.maxWidth = Integer.MAX_VALUE
//
//        val searchPlate: EditText =
//            searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
//        searchPlate.hint = "Search"
//        val searchPlateView: View =
//            searchView.findViewById(androidx.appcompat.R.id.search_plate)
//        searchPlateView.setBackgroundColor(
//            ContextCompat.getColor(
//                this,
//                android.R.color.transparent
//            )
//        )
//
//        val closeButton: ImageView = searchView.findViewById(R.id.search_close_btn) as ImageView
//        closeButton.setOnClickListener {
//            if (searchPlate.text.isNotEmpty()) {
//                searchPlate.setText("")
//                searchView.setQuery("", false)
//                searchItem.collapseActionView()
//            } else {
//                onBackPressed()
//            }
//        }
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                this@MainActivity.query = query
//                // TODO
////                productAdapter.filter.filter(query)
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                this@MainActivity.query = newText
//                // TODO
////                productAdapter.filter.filter(newText)
//                return false
//            }
//        })
//
//        val searchManager: SearchManager =
//            getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionSearch -> navigator.toSearchScreen(activityOrigin = this)
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        if (!searchView.isIconified) {
//            searchView.onActionViewCollapsed()
//        } else {
//            super.onBackPressed()
//        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.title = null
            it.setDisplayHomeAsUpEnabled(false)
        }
    }

    private fun setupPager() {
        val pagerAdapter = PagerMainFragmentAdapter(supportFragmentManager)
        pager.adapter = pagerAdapter
        pager.offscreenPageLimit = 5
        val fragmentList =
            itemMainPageList.map {
                it.fragment
            }
        pagerAdapter.setData(fragmentList)
    }

    private fun setupBottomNav() {
        bottomNav.setup(itemMainPageList, onBottomNavMainItemSelectedListener)
    }

    private val onBottomNavMainItemSelectedListener = object : ComponentBottomNav.OnBottomNavMainItemSelectedListener {
        override fun onItemSelected(view: View?, position: Int) {
            logErrorMessage("onItemSelected() $position")
            pager.setCurrentItem(position, false)
        }

        override fun onItemReselected(view: View?) {
            logErrorMessage("onItemReselected() $view")
        }

        override fun onItemUnselected(view: View?) {
            logErrorMessage("onItemUnselected() $view")
        }
    }

    /**
     * [MainTabProductListFragment.MainProductListFragmentListener] implementation.
     */
    override fun onSwipeActionAddProductToShopCart(product: ProductModel) {
        val observer = Observer<List<ShopCartModel>> { list ->
            if (list != null) {
                when (list.size) {
                    0 ->
                        Snackbar.make(mainLayout, getString(R.string.message_no_shop_cart_add_product_snack_bar), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.action_no_shop_cart_add_product_snack_bar)) {
                                ((pager.adapter as PagerMainFragmentAdapter).getItem(0) as MainHomeFragment)
                                    .goToShopCartTab()
                            }.show()
                    1 -> ComponentAddProductToShopCartDialog(this, list[0].id, product, addProductToCartDialogListener).show()
                    else -> {
                        // TODO Open dialog for user choose color/quantity
                        // If accept dialog and quantity != 0 -> Prompt user to choose shop cart to add product.
                    }
                }
            }
        }
        viewModel.liveDataOpenShopCarts.observe(this, observer)
    }

    /**
     * [MainProductListFragmentListener] implementation.
     */
    override fun onEditShopCartProductClick() {
        Toast.makeText(this, "onEditShopCartProductClick()", Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteShopCartProductClick() {
        Toast.makeText(this, "onDeleteShopCartProductClick()", Toast.LENGTH_SHORT).show()
    }

    override fun onCloseSaleClick(shopCart: ShopCartModel) {
        viewModel.liveDataSale.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    LiveDataStatusWrapper.Status.SUCCESS -> {
                        GlobalScope.launch {
                            viewModel.deleteShopCart(shopCart)
                        }
                    }
                }
            }
        })
        viewModel.closeShoppingCart(shopCart)
    }

    /**
     * [CreateShopCartDialogListener] implementation.
     */
    private val createShopCartDialogListener = object : CreateShopCartDialogListener {
        override fun onCreateShopCartDialogPositiveClick(name: String) {
            viewModel.createShopCart(name)
        }
    }

    /**
     * [MainHomeFragmentListener] implementation.
     */
    override fun onCreateProductClick() {
        navigator.toAddProductScreen(activityOrigin = this)
    }

    override fun onCreateShopCartClick() {
        ComponentCreateShopCartDialog(this, createShopCartDialogListener).show()
    }

    /**
     * [AddProductToShopCartDialogListener] implementation.
     */
    private val addProductToCartDialogListener: AddProductToShopCartDialogListener = object : AddProductToShopCartDialogListener {
        override fun onAddProductToShopCartDialogPositiveClick(
            shopCartId: Long,
            product: ShopCartModel.ShopCartProductModel
        ) {
            GlobalScope.launch {
                viewModel.addShopCartProduct(shopCartId, product)
            }
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