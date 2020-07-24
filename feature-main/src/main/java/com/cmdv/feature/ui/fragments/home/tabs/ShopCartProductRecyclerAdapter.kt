package com.cmdv.feature.ui.fragments.home.tabs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.core.helpers.formatPriceWithCurrency
import com.cmdv.domain.models.ShopCartModel.ShopCartProductModel
import com.cmdv.feature.R

class ShopCartProductRecyclerAdapter(
    private val context: Context,
    private val items: ArrayList<ShopCartProductModel>
) : RecyclerView.Adapter<ShopCartProductRecyclerAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.product_shop_cart_item, parent, false))

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindView(items[position], context, position)
    }

    override fun getItemCount(): Int = items.size

    /**
     * View Holder class.
     */
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewName = itemView.findViewById<AppCompatTextView>(R.id.textViewName)
        private val textViewQuantity = itemView.findViewById<AppCompatTextView>(R.id.textViewQuantity)
        private val textViewCode = itemView.findViewById<AppCompatTextView>(R.id.textViewCode)
        private val imageViewMoreButton = itemView.findViewById<AppCompatImageView>(R.id.imageViewMoreButton)
        private val textViewUnitPrice = itemView.findViewById<AppCompatTextView>(R.id.textViewUnitPrice)
        private val textViewTotalPrice = itemView.findViewById<AppCompatTextView>(R.id.textViewTotalPrice)

        private lateinit var product: ShopCartProductModel

        fun bindView(item: ShopCartProductModel, context: Context, position: Int) {
            this.product = item

            setName()
            setQuantity()
            setCode()
            setupOverflowMenu(context, position)
            setupUnitPrice()
            setupTotalPrice()
        }

        private fun setName() {
            textViewName.text = product.name
        }

        private fun setQuantity() {
            textViewQuantity.text = getProductQuantity().toString()
        }

        private fun setCode() {
            textViewCode.text = "#${product.code}"
        }

        private fun setupOverflowMenu(context: Context, position: Int) {
            imageViewMoreButton.setOnClickListener {
                openItemMenu(context, position)
            }
        }

        private fun setupUnitPrice() {
            textViewUnitPrice.text = "u.p. ${formatPriceWithCurrency(product.price.toFloat())}"
        }

        private fun setupTotalPrice() {
            textViewTotalPrice.text = formatPriceWithCurrency((getProductQuantity() * product.price.toDouble()).toFloat())
        }

        private fun getProductQuantity(): Int {
            var quantity = 0
            product.colorQuantity.forEach { colorQuantity ->
                quantity += colorQuantity.colorQuantity
            }
            return quantity
        }

        private fun openItemMenu(context: Context, position: Int) {
            PopupMenu(context, imageViewMoreButton).apply {
                inflate(R.menu.product_popup_menu)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.actionEditProduct -> {
                            (context as MainTabShopCartListFragment.MainTabShopCartListFragmentListener).onEditShopCartProductClick()
                        }
                        R.id.actionDeleteProduct -> {
                            (context as MainTabShopCartListFragment.MainTabShopCartListFragmentListener).onDeleteShopCartProductClick()
                        }
                    }
                    true
                }
                show()
            }
        }
    }

}