package com.cmdv.feature

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.cmdv.core.helpers.SimpleTextWatcher
import com.cmdv.core.helpers.formatPrice
import com.cmdv.core.utils.logErrorMessage
import com.cmdv.domain.models.Status
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_create_product.*
import kotlinx.android.synthetic.main.content_create_product.*
import org.koin.android.viewmodel.ext.android.viewModel

class CreateProductActivity : AppCompatActivity() {

    val viewModel: CreateProductActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_product)

        setupProductNameInputField()
        setupProductCostPriceInputField()
        setupProductOriginalPriceInputField()
        setupProductSellingPriceInputField()
        setupProductQuantityInputField()
        setupProductTagsInputField()
        setupAcceptButton()
    }

    private fun setupProductNameInputField() {
        editTextProductName.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.errorEmptyName.postValue(null)
                viewModel.name = s.toString()
            }
        })
        viewModel.errorEmptyName.observe(this, Observer { errorStringId ->
            manageInputError(errorStringId, editTextProductName, textInputProductName)
        })
    }

    private fun setupProductCostPriceInputField() {
        editTextProductCostPrice.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editTextProductCostPrice.let {
                    it.removeTextChangedListener(this)
                    viewModel.costPrice = sanitizePrice(s, it)
                    it.addTextChangedListener(this)
                }
            }
        })
        viewModel.errorEmptyCostPrice.observe(this, Observer { errorStringId ->
            manageInputError(errorStringId, editTextProductCostPrice, textInputProductCostPrice)
        })
    }

    private fun setupProductOriginalPriceInputField() {
        editTextProductOriginalPrice.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editTextProductOriginalPrice.let {
                    it.removeTextChangedListener(this)
                    viewModel.originalPrice = sanitizePrice(s, it)
                    it.addTextChangedListener(this)
                }
            }
        })
        viewModel.errorEmptyOriginalPrice.observe(this, Observer { errorStringId ->
            manageInputError(
                errorStringId,
                editTextProductOriginalPrice,
                textInputProductOriginalPrice
            )
        })
    }

    private fun setupProductSellingPriceInputField() {
        editTextProductSellingPrice.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editTextProductSellingPrice.let {
                    it.removeTextChangedListener(this)
                    viewModel.sellingPrice = sanitizePrice(s, it)
                    it.addTextChangedListener(this)
                }
            }
        })
        viewModel.errorEmptySellingPrice.observe(this, Observer { errorStringId ->
            manageInputError(
                errorStringId,
                editTextProductSellingPrice,
                textInputProductSellingPrice
            )
        })
    }

    private fun setupProductQuantityInputField() {
        editTextProductQuantity.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.quantity = if (!s.isNullOrEmpty()) {
                    s.toString().toInt()
                } else {
                    0
                }
            }
        })
        viewModel.errorEmptyQuantity.observe(this, Observer { errorStringId ->
            manageInputError(errorStringId, editTextProductQuantity, textInputProductQuantity)
        })
    }

    private fun setupProductTagsInputField() {
        editTextProductTags.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val tags = s.toString().replace(" ", "").split("#")
                viewModel.tags = tags
            }
        })
    }

    private fun setupAcceptButton() {
        buttonCreateProduct.setOnClickListener {
            viewModel.createProduct()?.observe(this, Observer {
                logErrorMessage("CreateProductActivity ---------> " + it?.status.toString() ?: "Product Creation Status Model null")
                when (it?.status) {
                    Status.LOADING -> { frameLoading.visibility = View.VISIBLE }
                    Status.SUCCESS -> {
                        frameLoading.visibility = View.GONE
                        clearValues()
                    }
                    Status.ERROR -> { frameLoading.visibility = View.GONE }
                    else -> { frameLoading.visibility = View.GONE }
                }
            })
        }
    }

    private fun sanitizePrice(s: CharSequence?, et: TextInputEditText): String {
        var formattedPrice = ""
        if (!s.isNullOrBlank()) {
            val price = s.toString().replace("$", "").replace(".", "").replace(".", "")
            if (price.isNotEmpty()) {
                formattedPrice = formatPrice(price.toFloat())
                et.apply {
                    setText(
                        StringBuilder(
                            String.format(
                                getString(R.string.placeholder_price),
                                formattedPrice
                            )
                        )
                    )
                    setSelection(et.text.toString().length)
                }
                return formattedPrice
            } else {
                et.setText(StringBuilder(""))
            }
        } else {
            et.setText(StringBuilder(""))
        }
        return formattedPrice
    }

    private fun manageInputError(errorStringId: Int?, et: TextInputEditText, til: TextInputLayout) {
        if (errorStringId != null) {
            et.error = ""
            til.apply {
                isErrorEnabled = true
                error = getString(errorStringId)
            }
        } else {
            et.error = null
            til.apply {
                error = null
                isErrorEnabled = false
            }
        }
    }

    private fun clearValues() {
        editTextProductName.text?.clear()
        editTextProductCostPrice.text?.clear()
        editTextProductOriginalPrice.text?.clear()
        editTextProductSellingPrice.text?.clear()
        editTextProductQuantity.text?.clear()
        editTextProductTags.text?.clear()
    }

}