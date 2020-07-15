package com.cmdv.components.colorpicker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.widget.AppCompatTextView
import com.cmdv.components.R

class SpinnerQuantityAdapter(private val context: Context) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var quantities: ArrayList<Int> = arrayListOf()

    init {
        for (i: Int in 1 until 99) {
            quantities.add(i)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.spinner_quantity_item, parent, false)
            holder = ViewHolder(view)
            view?.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        holder.bindItem(quantities[position])

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.spinner_dropdown_quantity_item, parent, false)
            holder = ViewHolder(view)
            view?.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        holder.bindItem(quantities[position])

        return view
    }

    override fun getItem(position: Int): Int =
        quantities[position]

    override fun getItemId(p0: Int): Long =
        0

    override fun getCount(): Int =
        quantities.size

    private class ViewHolder(row: View) {
        val textViewQuantity: AppCompatTextView = row.findViewById(R.id.textViewQuantity)

        fun bindItem(quantity: Int) {
            textViewQuantity.text = quantity.toString()
        }
    }

}