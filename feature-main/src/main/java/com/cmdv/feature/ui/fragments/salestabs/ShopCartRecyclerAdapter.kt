package com.cmdv.feature.ui.fragments.salestabs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.core.Constants
import com.cmdv.core.helpers.getDayMonthYearWithBars
import com.cmdv.core.helpers.getHoursMinutes
import com.cmdv.core.utils.collapseShopCartItemBody
import com.cmdv.core.utils.expandShopCartItemBody
import com.cmdv.domain.models.ShopCartModel
import com.cmdv.feature.R
import java.text.SimpleDateFormat
import java.util.*


class ShopCartRecyclerAdapter(private val context: Context) : RecyclerView.Adapter<ShopCartRecyclerAdapter.ShopCartViewHolder>() {

    private var items: List<ShopCartModel> = listOf()

    fun setItems(items: List<ShopCartModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopCartViewHolder =
        ShopCartViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.shop_cart_item, parent, false))

    override fun onBindViewHolder(holder: ShopCartViewHolder, position: Int) {
        holder.bindView(context, items[position])
    }

    override fun getItemCount(): Int =
        items.size

    class ShopCartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var shopCart: ShopCartModel

        // Header
        private val textViewOpenedDate = itemView.findViewById<AppCompatTextView>(R.id.textViewOpenedDate)
        private val textViewOpenedTime = itemView.findViewById<AppCompatTextView>(R.id.textViewOpenedTime)
        private val textViewName = itemView.findViewById<AppCompatTextView>(R.id.textViewName)
        private val imageButtonExpandCollapse = itemView.findViewById<ImageButton>(R.id.imageButtonExpandCollapse)

        // Footer
        private val textViewItemNumber = itemView.findViewById<AppCompatTextView>(R.id.textViewItemNumber)
        private val textViewSubtotal = itemView.findViewById<AppCompatTextView>(R.id.textViewSubtotal)
        private val textViewDiscount = itemView.findViewById<AppCompatTextView>(R.id.textViewDiscount)
        private val textViewTotal = itemView.findViewById<AppCompatTextView>(R.id.textViewTotal)

        // Body
        private val constraintBodyContainer = itemView.findViewById<ConstraintLayout>(R.id.constraintBodyContainer)
        private val textViewShopCartEmpty = itemView.findViewById<AppCompatTextView>(R.id.textViewShopCartEmpty)
        private val recyclerViewProducts = itemView.findViewById<RecyclerView>(R.id.recyclerViewProducts)
        private val buttonCloseShopCart = itemView.findViewById<AppCompatButton>(R.id.buttonCloseShopCart)

        fun bindView(context: Context, shopCart: ShopCartModel) {
            this.shopCart = shopCart
            setupHeader(context)
            setupBody()
            setupFooter(context)
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

        private fun setupBody() {
            if (shopCart.products.isEmpty()) {
                textViewShopCartEmpty.visibility = View.VISIBLE
                recyclerViewProducts.visibility = View.GONE
                buttonCloseShopCart.visibility = View.GONE
            } else {
                textViewShopCartEmpty.visibility = View.GONE
                recyclerViewProducts.visibility = View.VISIBLE
                buttonCloseShopCart.visibility = View.VISIBLE
            }
        }

        private fun setupFooter(context: Context) {
            textViewItemNumber.text = shopCart.products.size.toString()
            textViewSubtotal.text = String.format(
                context.resources.getString(R.string.placeholder_shop_cart_item_subtotal),
                getSubtotal()
            )
            textViewDiscount.text = String.format(
                context.resources.getString(R.string.placeholder_shop_cart_item_discount),
                getDiscount()
            )
            textViewTotal.text = String.format(
                context.resources.getString(R.string.placeholder_shop_cart_item_total),
                getTotal()
            )
        }

        private fun getSubtotal(): String {
            // TODO
            return "0.00"
        }

        private fun getDiscount(): String {
            // TODO
            return "0.00"
        }

        private fun getTotal(): String {
            // TODO
            return "0.00"
        }
    }

}

