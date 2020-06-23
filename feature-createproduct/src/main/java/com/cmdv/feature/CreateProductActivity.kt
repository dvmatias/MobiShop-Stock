package com.cmdv.feature

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.cmdv.core.helpers.HtmlHelper
import com.cmdv.core.helpers.KeyboardHelper
import com.cmdv.core.helpers.SimpleTextWatcher
import com.cmdv.core.helpers.formatPrice
import com.cmdv.domain.models.CreateProductStatusWrapper
import com.cmdv.domain.models.ProductModel
import com.cmdv.domain.models.Status
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_create_product.*
import kotlinx.android.synthetic.main.content_create_product.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.ref.WeakReference

class CreateProductActivity : AppCompatActivity() {

    val viewModel: CreateProductActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_product)

        setupExplanation()
        setupBackButton()
        setupProductNameInputField()
        setupProductCostPriceInputField()
        setupProductOriginalPriceInputField()
        setupProductSellingPriceInputField()
        setupProductQuantityInputField()
        setupProductTagsInputField()
        setupAcceptButton()
    }

    private fun setupExplanation() {
        textViewExplanation.text = HtmlHelper.fromHtml(R.string.create_product_explanation, this)
    }

    private fun setupBackButton() {
        imageViewBack.setOnClickListener { finish() }
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
                val tags: List<String> = s.toString().replace(" ", "").split("_")
                viewModel.tags.clear()
                for (tag in tags) {
                    with (tag) {
                        if (isNotEmpty() && !viewModel.tags.contains(this))  {
                            viewModel.tags.add(this)
                        }
                    }
                }
            }
        })
    }

    private fun setupAcceptButton() {
        buttonCreateProduct.setOnClickListener {
            KeyboardHelper.hideKeyboard(WeakReference(this), it)

            viewModel.createProduct()?.observe(this, Observer { productCreation ->
                when (productCreation?.status) {
                    Status.LOADING -> {
                        frameLoading.visibility = View.VISIBLE
                    }
                    Status.SUCCESS,
                    Status.ERROR -> {
                        frameLoading.visibility = View.GONE
                        setFeedbackScreen(productCreation)
                    }
                    else -> {
                    }
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

    private fun setFeedbackScreen(productCreation: CreateProductStatusWrapper<ProductModel?>?) {
        clearValues()

        val title: String
        val message: String
        var statusOk = false

        if (productCreation?.data == null || productCreation.status == Status.ERROR) {
            title = getString(R.string.dialog_title_ko)
            message = getString(R.string.dialog_message_ko)
        } else {
            title = getString(R.string.dialog_title_ok)
            message = getString(R.string.dialog_message_ok)
            statusOk = true
        }

        val dialog = Dialog(this)
        with(dialog) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.dialog_product_created_dialog)
            (this.findViewById(R.id.imageViewStatus) as AppCompatImageView).setImageDrawable(
                    ContextCompat.getDrawable(
                            this@CreateProductActivity,
                            if (statusOk) R.drawable.ic_dialog_ok_32 else R.drawable.ic_dialog_ko_32
                    )
            )
            (this.findViewById(R.id.textViewTitle) as AppCompatTextView).text = title
            (this.findViewById(R.id.textViewMessage) as AppCompatTextView).text = message
            if (statusOk) {
                (this.findViewById(R.id.textViewProductNameValue) as AppCompatTextView).text =
                        productCreation?.data?.name ?: ""
                (this.findViewById(R.id.textViewProductCodeValue) as AppCompatTextView).text =
                        productCreation?.data?.code ?: ""
            } else {
                (this.findViewById(R.id.textViewProductNameTitle) as AppCompatTextView).visibility = View.GONE
                (this.findViewById(R.id.textViewProductNameValue) as AppCompatTextView).visibility = View.GONE
                (this.findViewById(R.id.textViewProductCodeTitle) as AppCompatTextView).visibility = View.GONE
                (this.findViewById(R.id.textViewProductCodeValue) as AppCompatTextView).visibility = View.INVISIBLE
            }
            (this.findViewById(R.id.buttonNegative) as AppCompatButton).setOnClickListener {
                dialog.dismiss()
                finish()
            }
            (this.findViewById(R.id.buttonPositive) as AppCompatButton).setOnClickListener {
                dialog.dismiss()
            }
            show()
            window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun clearValues() {
        editTextProductName.text?.clear()
        editTextProductCostPrice.text?.clear()
        editTextProductOriginalPrice.text?.clear()
        editTextProductSellingPrice.text?.clear()
        editTextProductQuantity.text?.clear()
        editTextProductTags.text?.clear()
        scrollView.requestFocus()
    }

}