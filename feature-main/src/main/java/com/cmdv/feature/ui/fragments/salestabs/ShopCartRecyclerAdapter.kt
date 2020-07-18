package com.cmdv.feature.ui.fragments.salestabs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.domain.models.ShopCartModel
import com.cmdv.feature.R

class ShopCartRecyclerAdapter : RecyclerView.Adapter<ShopCartRecyclerAdapter.ShopCartViewHolder>() {

    private var items: List<ShopCartModel> = listOf()

    fun setItems(items: List<ShopCartModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopCartViewHolder =
        ShopCartViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.shop_cart_item , parent, false))

    override fun onBindViewHolder(holder: ShopCartViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int =
        items.size

    class ShopCartViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val textViewMock =
            itemView.findViewById<AppCompatTextView>(R.id.textViewMock)

        fun bindView(shopCart: ShopCartModel) {
            textViewMock.text = shopCart.id.toString()
        }
    }

}