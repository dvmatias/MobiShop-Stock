package com.cmdv.feature

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.cmdv.components.colorpicker.Mode
import com.cmdv.core.Constants.Companion.EXTRA_PRODUCT_KEY
import com.cmdv.core.helpers.ProductQuantityHelper
import com.cmdv.core.helpers.SimpleTextWatcher
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.ProductModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_edit_product.*
import kotlinx.android.synthetic.main.content_edit_product.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class EditProductActivity : AppCompatActivity() {

    private val viewModel: EditProductActivityViewModel by viewModel()

    private val gson: Gson by inject()

    private var product: ProductModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        getExtras()
        setupToolbar()

        setupProductNameInputField()
        setupProductTypeInputField()
        setupProductDescriptionInputField()
        setupProductPricesInputField()
        setupProductQuantityInputField()
        setupComponentColorQuantityView()
    }

    private fun getExtras() {
        intent.extras?.let {
            if (it.containsKey(EXTRA_PRODUCT_KEY)) {
                product =
                    gson.fromJson(it.getString(EXTRA_PRODUCT_KEY), ProductModel::class.java)
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.title = null
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupProductNameInputField() {
        editTextProductName.apply {
            addTextChangedListener(object : SimpleTextWatcher() {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.name = s?.toString() ?: ""
                }
            })
            setText(product?.name)
        }
    }

    private fun setupProductTypeInputField() {
        // TODO
    }

    private fun setupProductDescriptionInputField() {
        editTextProductDescription.apply {
            addTextChangedListener(object : SimpleTextWatcher() {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.description = s?.toString() ?: ""
                }
            })
            setText(product?.description)
        }
    }

    private fun setupProductPricesInputField() {
        product?.price?.let { price ->
            editTextProductCostPrice.apply {
                addTextChangedListener(object : SimpleTextWatcher() {
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        viewModel.costPrice = s?.toString() ?: ""
                    }
                })
                setText(price.costPrice)
            }
            editTextProductSellingPrice.apply {
                addTextChangedListener(object : SimpleTextWatcher() {
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        viewModel.sellingPrice = s?.toString() ?: ""
                    }
                })
                setText(price.sellingPrice)
            }
            editTextProductOriginalPrice.apply {
                addTextChangedListener(object : SimpleTextWatcher() {
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        viewModel.originalPrice = s?.toString() ?: ""
                    }
                })
                setText(price.originalPrice)
            }
        }
    }

    private fun setupProductQuantityInputField() {
        product?.quantity?.let { quantity ->
            editTextProductQuantityAvailable.apply {
                setText(ProductQuantityHelper.getQuantityAvailable(quantity.colorQuantities).toString())
            }
            editTextProductQuantitySold.apply {
                addTextChangedListener(object : SimpleTextWatcher() {
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        s?.let {
                            viewModel.soldQuantity =
                                if (s.isEmpty()) -1 else s.toString().toInt()
                        } ?: run { viewModel.soldQuantity = -1 }
                    }
                })
                setText(quantity.sold.toString())
            }
        }
    }

    private fun setupComponentColorQuantityView() {
        componentColorQuantityView.setup(this, Mode.EDIT, product?.quantity?.colorQuantities)
        componentColorQuantityView.mutableLiveColorQuantityList.observe(this, Observer {
            viewModel.colorQuantities = ArrayList(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_product, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.actionUpdateProduct -> {
                viewModel.productLiveData.observe(this, Observer { updateProductWrapper ->
                    when (updateProductWrapper.status) {
                        LiveDataStatusWrapper.Status.LOADING -> {
                            frameLoading.visibility = View.VISIBLE
                        }
                        LiveDataStatusWrapper.Status.SUCCESS -> {
                            frameLoading.visibility = View.GONE
                            onBackPressed()
                        }
                        LiveDataStatusWrapper.Status.ERROR -> {
                            // TODO Manage scenario.
                            frameLoading.visibility = View.GONE
                        }
                    }
                })
                product?.let { viewModel.updateProduct(it) }
            }
        }
        return true
    }

}