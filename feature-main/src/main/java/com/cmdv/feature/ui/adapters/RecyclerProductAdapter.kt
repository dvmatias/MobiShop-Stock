package com.cmdv.feature.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.domain.models.ProductModel
import com.cmdv.feature.R
import java.util.*

enum class ItemType(val type: Int) {
    SECTION(0),
    PRODUCT(1)
}

class RecyclerProductAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var products: ArrayList<ProductModel> = arrayListOf()
    private var sections: ArrayList<String> = arrayListOf()
    private var data: ArrayList<Any> = arrayListOf()
    private lateinit var fullData: ArrayList<ProductModel>
    private var isFullDataLoaded = false

    fun setProducts(products: ArrayList<ProductModel>) {
        if (!isFullDataLoaded) {
            fullData = arrayListOf()
            fullData.addAll(products)
            isFullDataLoaded = true
        }
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
                    .inflate(R.layout.item_section, parent, false)
                SectionViewHolder(view)
            }
            ItemType.PRODUCT.type -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_product_list_thin, parent, false)
                ProductViewHolder(view)
            }
            else -> {
                throw IllegalStateException("")
            }
        }
    }

    override fun getItemCount(): Int =
        this.data.size

    override fun getItemViewType(position: Int): Int {
        return if (data[position] is String) ItemType.SECTION.type else ItemType.PRODUCT.type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ItemType.SECTION.type -> {
                (holder as SectionViewHolder).bindView(data[position] as String)
            }
            ItemType.PRODUCT.type -> {
                (holder as ProductViewHolder).bindView(data[position] as ProductModel, context)
            }
        }
    }

    /**
     * Product view holder.
     */
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewProductName: AppCompatTextView =
            itemView.findViewById(R.id.textViewProductName)
        private val textViewProductDescription: AppCompatTextView =
            itemView.findViewById(R.id.textViewProductDescription)
        private val textViewProductSellingPrice: AppCompatTextView =
            itemView.findViewById(R.id.textViewProductSellingPrice)
        private val textViewProductCostPrice: AppCompatTextView =
            itemView.findViewById(R.id.textViewProductCostPrice)
        private val textViewProductCode: AppCompatTextView =
            itemView.findViewById(R.id.textViewProductCode)

        fun bindView(product: ProductModel, context: Context) {
            with(product) {
                // TODO product image
                textViewProductName.text = name
                textViewProductDescription.text = description
                textViewProductSellingPrice.text = String.format(context.getString(R.string.item_product_price_placeholder), price.sellingPrice)
                textViewProductCostPrice.text = String.format(context.getString(R.string.item_product_price_placeholder), price.costPrice)
                textViewProductCode.text = String.format(context.getString(R.string.item_product_code_placeholder), code)
            }

            // TODO add buttons functionality: imageViewDeleteProductButton imageViewEditProductButton
        }

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
     * Filterable implementation.
     */
    override fun getFilter(): Filter {
        return ProductFilter(this, fullData)
    }

}