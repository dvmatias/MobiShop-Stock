package com.cmdv.components.dialog.addproducttoshopcart

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.components.R
import com.cmdv.core.Constants
import com.cmdv.core.helpers.DisplayHelper
import com.cmdv.domain.models.ProductModel
import com.cmdv.domain.models.ShopCartModel
import kotlinx.android.synthetic.main.dialog_add_product_to_shop_cart_component.*

class ComponentAddProductToShopCartDialog(
    context: Context,
    private val product: ProductModel,
    private val listener: AddProductToShopCartDialogListener
) : Dialog(context) {

    private lateinit var recyclerAdapter: AddProductToShopCartDialogColorQuantityRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_add_product_to_shop_cart_component)

        setupWindowParams()
        setupTitle()
        setupRecycler()
        setupButtons()
        setTotal(0)
    }

    private fun setupWindowParams() {
        window?.apply {
            setLayout(
                (DisplayHelper.getDisplayWidthPx() * Constants.DIALOG_WIDTH_DISPLAY_PERCENTAGE).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        setCancelable(false)
    }

    private fun setupTitle() {
        textViewTitle.text = product.name
    }

    private fun setupRecycler() {
        recyclerAdapter = AddProductToShopCartDialogColorQuantityRecyclerAdapter(context, product.quantity.colorQuantities, totalChangedListener)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = recyclerAdapter
        }
        recyclerAdapter.notifyDataSetChanged()
    }

    private fun setupButtons() {
        buttonNegative.setOnClickListener { dismiss() }
        buttonPositive.setOnClickListener {
            val productToAdd: ShopCartModel.ShopCartProductModel? = getProductToAdd()
            if (productToAdd != null) {
                listener.onAddProductToShopCartDialogPositiveClick(productToAdd)
            }
            dismiss()
        }
    }

    private fun setTotal(total: Int) {
        textViewTotal.text = total.toString()
    }

    private fun getProductToAdd(): ShopCartModel.ShopCartProductModel? {
        val colorQuantities: List<ShopCartModel.ShopCartProductColorQuantityDatabaseModel> =
            recyclerAdapter.getColorQuantity()

        if (colorQuantities.isEmpty()) return null

        return ShopCartModel.ShopCartProductModel(
            product.code,
            product.name,
            product.price.sellingPrice,
            product.imageName,
            colorQuantities
        )
    }

    /**
     * [OnTotalChangedListener] implementation
     */
    private val totalChangedListener = object : OnTotalChangedListener {
        override fun updateTotal(total: Int) {
            setTotal(total)
        }
    }

    /**
     *
     */
    interface OnTotalChangedListener {
        fun updateTotal(total: Int)
    }

}