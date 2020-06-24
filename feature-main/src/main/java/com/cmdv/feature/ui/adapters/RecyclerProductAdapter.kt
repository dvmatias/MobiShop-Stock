package com.cmdv.feature.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.domain.models.ProductModel
import com.cmdv.feature.R

class RecyclerProductAdapter : RecyclerView.Adapter<RecyclerProductAdapter.ProductViewHolder>() {

    private var data: ArrayList<ProductModel> = arrayListOf()

    fun setData(data: ArrayList<ProductModel>) {
        this.data.clear()
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_list_thin, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int =
        this.data.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindView(data[position])
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewProductName: AppCompatTextView = itemView.findViewById(R.id.textViewProductName)
        private val textViewProductDescription: AppCompatTextView = itemView.findViewById(R.id.textViewProductDescription)
        private val textViewProductSellingPrice: AppCompatTextView = itemView.findViewById(R.id.textViewProductSellingPrice)
        private val textViewProductCostPrice: AppCompatTextView = itemView.findViewById(R.id.textViewProductCostPrice)

        fun bindView(product: ProductModel) {
            with (product) {
                // TODO product image
                textViewProductName.text = name
                textViewProductDescription.text = description
                textViewProductSellingPrice.text = "$ ${price.sellingPrice}"
                textViewProductCostPrice.text = "$ ${price.costPrice}"
            }

            // TODO add buttons functionality: imageViewDeleteProductButton imageViewEditProductButton
        }

    }

}