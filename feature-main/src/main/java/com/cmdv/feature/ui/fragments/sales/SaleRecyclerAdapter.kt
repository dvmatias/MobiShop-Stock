package com.cmdv.feature.ui.fragments.sales

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.domain.models.SaleModel
import com.cmdv.feature.R

class SaleRecyclerAdapter : RecyclerView.Adapter<SaleRecyclerAdapter.SaleViewHolder>() {

    private var items: List<SaleModel> = listOf()

    fun setItems(items: List<SaleModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleViewHolder =
        SaleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.sale_recycler_item, parent, false))

    override fun onBindViewHolder(holder: SaleViewHolder, position: Int) {
        holder.bindItem(items[position])
    }

    override fun getItemCount(): Int = items.size

    class SaleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(sale: SaleModel) {

        }

    }

}
