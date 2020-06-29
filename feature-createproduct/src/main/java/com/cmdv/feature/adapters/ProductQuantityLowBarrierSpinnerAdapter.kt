package com.cmdv.feature.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.cmdv.feature.R

class ProductQuantityLowBarrierSpinnerAdapter(private val context: Context, private val lowBarriers: ArrayList<String>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    init {
        // Ad hint text.
        lowBarriers.add(0, context.getString(R.string.product_quantity_low_barrier_hint))
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_product_quantity_low_barrier, parent, false)
            holder = ViewHolder(view)
            view?.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        holder.bindItem(lowBarriers[position], position, context)

        return view
    }

    override fun getItem(position: Int): Any =
        this.lowBarriers[position]

    override fun getItemId(p0: Int): Long =
        0

    override fun getCount(): Int =
        this.lowBarriers.size


    private class ViewHolder(row: View?) {

        val label: AppCompatTextView = row?.findViewById(R.id.textViewProductQuantityLowBarrier) as AppCompatTextView

        fun bindItem(productType: String, position: Int, context: Context) {
            label.text = productType
            if (position == 0) {
                label.setTextColor(ContextCompat.getColor(context, R.color.colorTextHintDark))
            } else {
                label.setTextColor(ContextCompat.getColor(context, R.color.colorTextDark1))
            }
        }

    }

}