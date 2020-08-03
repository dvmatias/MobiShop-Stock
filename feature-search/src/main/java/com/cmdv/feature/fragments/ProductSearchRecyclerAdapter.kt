package com.cmdv.feature.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.core.helpers.ProductQuantityHelper
import com.cmdv.domain.models.ProductModel
import com.cmdv.feature.R
import java.util.*

enum class ItemType(val type: Int) {
    SECTION(0),
    PRODUCT(1)
}

class ProductSearchRecyclerAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var products: ArrayList<ProductModel> = arrayListOf()
    private var sections: ArrayList<String> = arrayListOf()
    private var data: ArrayList<Any> = arrayListOf()

    fun setProducts(products: ArrayList<ProductModel>) {
        this.products.clear()
        this.products = products
        setSections()
        setData()
        notifyDataSetChanged()
    }

    private fun setSections() {
        this.sections.clear()
        for (product in products) {
            if (!sections.contains(product.productType)) {
                sections.add(product.productType)
            }
        }
        Collections.sort(sections, String.CASE_INSENSITIVE_ORDER)
    }

    private fun setData() {
        this.data.clear()
        for (section in sections) {
            if (!data.contains(section)) {
                data.add(section)
                for (product in products) {
                    if (product.productType == section) {
                        data.add(product)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ItemType.SECTION.type -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_search_product_section_recycler, parent, false)
                SectionViewHolder(view)
            }
            ItemType.PRODUCT.type -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_search_product_recycler, parent, false)
                ProductViewHolder(view)
            }
            else -> {
                throw IllegalStateException("")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ItemType.SECTION.type -> {
                (holder as SectionViewHolder).bindView(data[position] as String)
            }
            ItemType.PRODUCT.type -> {
                (holder as ProductViewHolder).bindItem(data[position] as ProductModel, context, position)
            }
        }
    }

    override fun getItemCount(): Int =
        this.data.size

    override fun getItemViewType(position: Int): Int {
        return if (data[position] is String) ItemType.SECTION.type else ItemType.PRODUCT.type
    }

    /**
     * Section view holder.
     */
    class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewProductName: AppCompatTextView =
            itemView.findViewById(R.id.textViewSectionName)

        fun bindView(section: String) {
            textViewProductName.text = section
        }
    }

    /**
     * Product View Holder class.
     */
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewAddProductToShopCartButton = itemView.findViewById<AppCompatImageView>(R.id.imageViewAddProductToShopCartButton)
        private val textViewProductName: AppCompatTextView = itemView.findViewById(R.id.textViewProductName)
        private val textViewProductSellingPrice: AppCompatTextView = itemView.findViewById(R.id.textViewProductSellingPrice)
        private val textViewProductCostPrice: AppCompatTextView = itemView.findViewById(R.id.textViewProductCostPrice)
        private val textViewProductCode: AppCompatTextView = itemView.findViewById(R.id.textViewProductCode)
        private val textViewAvailableQuantity: AppCompatTextView = itemView.findViewById(R.id.textViewAvailableQuantity)

        private lateinit var product: ProductModel
        private var quantityAvailable: Int = 0

        fun bindItem(
            product: ProductModel,
            context: Context,
            position: Int
        ) {
            this.product = product
            this.quantityAvailable = ProductQuantityHelper.getQuantityAvailable(product.quantity.colorQuantities)
            // TODO product image
            textViewProductName.text = this.product.name
            textViewProductSellingPrice.text =
                String.format(context.getString(R.string.item_product_price_placeholder), this.product.price.sellingPrice)
            textViewProductCostPrice.text = String.format(context.getString(R.string.item_product_price_placeholder), this.product.price.costPrice)
            textViewProductCode.text = String.format(context.getString(R.string.item_product_code_placeholder), this.product.code)
            textViewAvailableQuantity.text = this.quantityAvailable.toString()
//            setupAddProductToShopCart(productItemListener, position) TODO
        }

    }

}
