package com.cmdv.feature.ui.fragments.sales

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.core.Constants
import com.cmdv.core.helpers.formatPriceWithCurrency
import com.cmdv.core.helpers.getDayMonthYearWithBars
import com.cmdv.core.helpers.getHoursMinutes
import com.cmdv.domain.models.SaleModel
import com.cmdv.feature.R
import java.text.SimpleDateFormat
import java.util.*

class SaleRecyclerAdapter(private val context: Context) : RecyclerView.Adapter<SaleRecyclerAdapter.SaleViewHolder>() {

    private var items: List<SaleModel> = listOf()

    fun setItems(items: List<SaleModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleViewHolder =
        SaleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.sale_recycler_item, parent, false))

    override fun onBindViewHolder(holder: SaleViewHolder, position: Int) {
        holder.bindItem(items[position], context)
    }

    override fun getItemCount(): Int = items.size

    class SaleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewName: AppCompatTextView = itemView.findViewById(R.id.textViewName)
        private val textViewClosedDate: AppCompatTextView = itemView.findViewById(R.id.textViewClosedDate)
        private val textViewClosedTime: AppCompatTextView = itemView.findViewById(R.id.textViewClosedTime)
        private val textViewQuantity: AppCompatTextView = itemView.findViewById(R.id.textViewQuantity)
        private val textViewTotalPrice: AppCompatTextView = itemView.findViewById(R.id.textViewTotalPrice)

        fun bindItem(sale: SaleModel, context: Context) {
            with (sale) {
                textViewName.text = this.name
                textViewClosedDate.text = getDayMonthYearWithBars(
                    this.date.createdDate,
                    SimpleDateFormat(Constants.DATE_FORMAT_DD_MM_YY_HH_MM_SS, Locale.getDefault())
                )
                textViewClosedTime.text = String.format(
                    context.resources.getString(R.string.placeholder_shop_cart_item_opened_time),
                    getHoursMinutes(
                        this.date.closedDate,
                        SimpleDateFormat(Constants.DATE_FORMAT_DD_MM_YY_HH_MM_SS, Locale.getDefault())
                    )
                )
                textViewQuantity.text = this.productQuantity.toString()
                textViewTotalPrice.text = formatPriceWithCurrency(this.total)
            }
        }

    }

}
