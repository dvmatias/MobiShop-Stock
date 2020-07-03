package com.cmdv.components.colorquantity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.cmdv.components.R


class SpinnerColorQuantityAdapter(private val context: Context) : BaseAdapter() {

    private var data: ArrayList<String> = arrayListOf()

    init {
        data.add("Quantity")
        for (i in 1..999) {
            data.add(i.toString())
        }
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: QuantityViewHolder
        if (null == convertView) {
            view = inflater.inflate(R.layout.spinner_quantity_item, parent, false)
            holder = QuantityViewHolder(view)
            view?.tag = holder
        } else {
            view = convertView
            holder = view.tag as QuantityViewHolder
        }

        holder.bindView(this.data[position], position, context)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: QuantityViewHolder
        if (null == convertView) {
            view = inflater.inflate(R.layout.spinner_dropdown_quantity_item, parent, false)
            holder = QuantityViewHolder(view)
            view?.tag = holder
        } else {
            view = convertView
            holder = view.tag as QuantityViewHolder
        }

        holder.bindView(this.data[position], position, context)
        return view
    }

    override fun getItem(position: Int): String =
        this.data[position]

    override fun getItemId(p0: Int): Long =
        0

    override fun getCount(): Int =
        this.data.size

    fun getItemPosition(value: String): Int {
        for (i in 0 until (this.data.size - 1)) {
            if (data[i] == value) {
                return i
            }
        }
        return 0
    }


    private class QuantityViewHolder(itemView: View) {
        val textViewColorQuantity: AppCompatTextView =
            itemView.findViewById(R.id.textViewColorQuantity)

        fun bindView(quantity: String, position: Int, context: Context) {
            textViewColorQuantity.text = quantity
            if (position == 0) {
                textViewColorQuantity.setTextColor(ContextCompat.getColor(context, R.color.colorTextHintDark))
            } else {
                textViewColorQuantity.setTextColor(ContextCompat.getColor(context, R.color.colorTextDark1))
            }
        }

    }

}