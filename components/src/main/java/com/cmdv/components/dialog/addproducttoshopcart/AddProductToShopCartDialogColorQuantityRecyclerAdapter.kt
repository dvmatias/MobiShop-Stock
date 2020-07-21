package com.cmdv.components.dialog.addproducttoshopcart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.components.R
import com.cmdv.domain.models.ColorQuantityModel
import com.cmdv.domain.models.ShopCartModel

class AddProductToShopCartDialogColorQuantityRecyclerAdapter(
    private val context: Context,
    private val colorQuantities: ArrayList<ColorQuantityModel>,
    private val totalChangedListener: ComponentAddProductToShopCartDialog.OnTotalChangedListener
) : RecyclerView.Adapter<AddProductToShopCartDialogColorQuantityRecyclerAdapter.ColorQuantityViewHolder>() {

    private var items: ArrayList<Pair<String, Int>> = arrayListOf()

    init {
        colorQuantities.forEach {
            items.add(Pair(it.value, 0))
        }
    }

    private fun updateTotal() {
        var total = 0
        items.forEach {
            total += it.second
        }
        totalChangedListener.updateTotal(total)
    }

    private val listener: ProductQuantityListener = object : ProductQuantityListener {
        override fun onAddOne(position: Int) {
            val item: Pair<String, Int> = items[position]
            val max: Int = colorQuantities[position].quantity
            if (item.second < max) {
                val new: Int = item.second + 1
                val pair = item.copy(item.first, new)
                items.removeAt(position)
                items.add(position, pair)
                notifyDataSetChanged()
                updateTotal()
            }
        }

        override fun onSubtractOne(position: Int) {
            val item: Pair<String, Int> = items[position]
            val min = 0
            if (item.second > min) {
                val new: Int = item.second - 1
                val pair = item.copy(item.first, new)
                items.removeAt(position)
                items.add(position, pair)
                notifyDataSetChanged()
                updateTotal()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorQuantityViewHolder =
        ColorQuantityViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.color_quantity_add_product_to_shop_cart_item, parent, false))

    override fun onBindViewHolder(holder: ColorQuantityViewHolder, position: Int) {
        holder.bindItem(items[position], context, listener, position, colorQuantities[position].quantity)
    }

    override fun getItemCount(): Int = colorQuantities.size

    fun getColorQuantity(): List<ShopCartModel.ShopCartProductColorQuantityDatabaseModel> {
        val colorQuantities: ArrayList<ShopCartModel.ShopCartProductColorQuantityDatabaseModel> = arrayListOf()
        items.forEach {  item ->
            if (item.second > 0) {
                colorQuantities.add(
                    ShopCartModel.ShopCartProductColorQuantityDatabaseModel(item.first, item.second)
                )
            }
        }
        return colorQuantities
    }

    /**
     * View Holder
     */
    class ColorQuantityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardViewColor = itemView.findViewById<CardView>(R.id.cardViewColor)
        private val imageViewTransparentEffect: AppCompatImageView = itemView.findViewById(R.id.imageViewTransparentEffect)
        private val imageButtonSubtract = itemView.findViewById<AppCompatImageButton>(R.id.imageButtonSubtract)
        private val imageButtonAdd = itemView.findViewById<AppCompatImageButton>(R.id.imageButtonAdd)
        private val editTextQuantity = itemView.findViewById<AppCompatTextView>(R.id.textViewQuantity)
        private val textViewMaxQuantity = itemView.findViewById<AppCompatTextView>(R.id.textViewMaxQuantity)

        fun bindItem(item: Pair<String, Int>, context: Context, listener: ProductQuantityListener, position: Int, max: Int) {
            editTextQuantity.text = item.second.toString()
            imageButtonAdd.setOnClickListener { listener.onAddOne(position) }
            imageButtonSubtract.setOnClickListener { listener.onSubtractOne(position) }
            textViewMaxQuantity.text = String.format(context.resources.getString(R.string.placeholder_add_product_to_shop_cart_dialog_max), max)
            if (item.first.toInt() != 0) {
                cardViewColor.setCardBackgroundColor(item.first.toInt())
                imageViewTransparentEffect.visibility = View.GONE
            } else {
                cardViewColor.setCardBackgroundColor(-1)
                imageViewTransparentEffect.visibility = View.VISIBLE
            }
            cardViewColor.setCardBackgroundColor(if (item.first != "0") item.first.toInt() else -1)

        }

    }

    interface ProductQuantityListener {
        fun onAddOne(position: Int)
        fun onSubtractOne(position: Int)
    }

}