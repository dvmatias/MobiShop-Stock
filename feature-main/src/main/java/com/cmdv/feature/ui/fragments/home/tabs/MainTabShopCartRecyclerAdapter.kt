package com.cmdv.feature.ui.fragments.home.tabs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.core.Constants
import com.cmdv.core.helpers.formatPriceWithCurrency
import com.cmdv.core.helpers.getDayMonthYearWithBars
import com.cmdv.core.helpers.getHoursMinutes
import com.cmdv.core.utils.collapseShopCartItemBody
import com.cmdv.core.utils.expandShopCartItemBody
import com.cmdv.domain.models.ShopCartModel
import com.cmdv.feature.R
import com.cmdv.feature.ui.fragments.home.tabs.MainTabShopCartListFragment.MainTabShopCartListFragmentListener
import java.text.SimpleDateFormat
import java.util.*


class MainTabShopCartRecyclerAdapter(
    private val context: Context,
    private val shopCartListFragmentListener: MainTabShopCartListFragmentListener
) : RecyclerView.Adapter<MainTabShopCartRecyclerAdapter.ShopCartViewHolder>() {

    private var items: List<ShopCartModel> = listOf()

    fun setItems(items: List<ShopCartModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopCartViewHolder =
        ShopCartViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.shop_cart_item, parent, false)
        )

    override fun onBindViewHolder(holder: ShopCartViewHolder, position: Int) {
        holder.bindView(context, items[position], shopCartListFragmentListener)
    }

    override fun getItemCount(): Int =
        items.size

    class ShopCartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var shopCart: ShopCartModel
        private lateinit var productAdapter: ShopCartProductRecyclerAdapter
        private lateinit var shopCartListFragmentListener: MainTabShopCartListFragmentListener
        // Header
        private val textViewOpenedDate = itemView.findViewById<AppCompatTextView>(R.id.textViewOpenedDate)
        private val textViewOpenedTime = itemView.findViewById<AppCompatTextView>(R.id.textViewOpenedTime)
        private val textViewName = itemView.findViewById<AppCompatTextView>(R.id.textViewName)
        private val imageButtonExpandCollapse = itemView.findViewById<ImageButton>(R.id.imageButtonExpandCollapse)
        // Body
        private val constraintBodyContainer = itemView.findViewById<ConstraintLayout>(R.id.constraintBodyContainer)
        private val textViewShopCartEmpty = itemView.findViewById<AppCompatTextView>(R.id.textViewShopCartEmpty)
        private val recyclerProduct = itemView.findViewById<RecyclerView>(R.id.recyclerProduct)
        // Footer
        private val textViewItemNumber = itemView.findViewById<AppCompatTextView>(R.id.textViewItemNumber)
        private val textViewSubtotal = itemView.findViewById<AppCompatTextView>(R.id.textViewSubtotal)
        private val textViewDiscount = itemView.findViewById<AppCompatTextView>(R.id.textViewDiscount)
        private val textViewTotal = itemView.findViewById<AppCompatTextView>(R.id.textViewTotal)
        private val buttonCloseSale = itemView.findViewById<AppCompatButton>(R.id.buttonCloseSale)

        fun bindView(context: Context, shopCart: ShopCartModel, shopCartListFragmentListener: MainTabShopCartListFragmentListener) {
            this.shopCart = shopCart
            this.shopCartListFragmentListener = shopCartListFragmentListener

            setupHeader(context)
            setupBody(context)
            setupFooter()
        }

        private fun setupHeader(context: Context) {
            textViewOpenedDate.text = getDayMonthYearWithBars(
                this.shopCart.date.createdDate,
                SimpleDateFormat(Constants.DATE_FORMAT_DD_MM_YY_HH_MM_SS, Locale.getDefault())
            )
            textViewOpenedTime.text = String.format(
                context.resources.getString(R.string.placeholder_shop_cart_item_opened_time),
                getHoursMinutes(
                    this.shopCart.date.createdDate,
                    SimpleDateFormat(Constants.DATE_FORMAT_DD_MM_YY_HH_MM_SS, Locale.getDefault())
                )
            )
            textViewName.text = this.shopCart.name
            imageButtonExpandCollapse.setOnClickListener {
                constraintBodyContainer.tag?.let { tag ->
                    when (tag) {
                        "collapsed", "collapsing" -> expandShopCartItemBody(constraintBodyContainer)
                        "expanded", "expanding" -> collapseShopCartItemBody(constraintBodyContainer)
                    }
                }
            }
        }

        private fun setupBody(context: Context) {
            if (shopCart.products.isEmpty()) {
                textViewShopCartEmpty.visibility = View.VISIBLE
                recyclerProduct.visibility = View.GONE
            } else {
                textViewShopCartEmpty.visibility = View.GONE
                recyclerProduct.visibility = View.VISIBLE
                setupRecyclerProduct(context)
            }
        }

        private fun setupFooter() {
            textViewItemNumber.text = shopCart.products.size.toString()
            textViewSubtotal.text = formatPriceWithCurrency(getSubtotal())
            textViewDiscount.text = formatPriceWithCurrency(getDiscount())
            textViewTotal.text = formatPriceWithCurrency(getTotal())
            setupCloseSaleButton()
        }

        private fun setupCloseSaleButton() {
            if (shopCart.products.isNotEmpty()) {
                buttonCloseSale.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        shopCartListFragmentListener.onCloseSaleClick(shopCart)
                    }
                }
            } else {
                buttonCloseSale.visibility = View.GONE
            }
        }

        private fun getSubtotal(): Float {
            var subtotal = 0F
            shopCart.products.forEach { product ->
                var quantity = 0
                product.colorQuantity.forEach { colorQuantity ->
                    quantity += colorQuantity.colorQuantity
                }
                subtotal += quantity * product.price.toFloat()
            }
            return subtotal
        }

        private fun getDiscount(): Float = 0F

        private fun getTotal(): Float = getSubtotal() - getDiscount()

        private fun setupRecyclerProduct(context: Context) {
            productAdapter = ShopCartProductRecyclerAdapter(context, shopCart.products)
            recyclerProduct.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = productAdapter
            }
        }
    }

}

