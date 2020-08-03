package com.cmdv.components.colorpicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.components.R
import com.cmdv.domain.models.ProductModel

/**
 * Adapter to display [ProductModel.ColorQuantityModel] item list.
 *
 * @see ProductModel.ColorQuantityModel.name: Color name [String]
 * @see ProductModel.ColorQuantityModel.value: Color value [String]
 * @see ProductModel.ColorQuantityModel.quantity: Color quantity [Int]
 */
class RecyclerColoQuantityAdapter(private val clickListener: OnColorQuantityClickListener) : RecyclerView.Adapter<RecyclerColoQuantityAdapter.ColorQuantityHolder>() {

    private var items: ArrayList<ProductModel.ColorQuantityModel> = arrayListOf()

    /**
     * Init items to display.
     */
    fun setItems(items: ArrayList<ProductModel.ColorQuantityModel>) {
        this.items.apply {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }

    /**
     * Add an item.
     */
    fun addItem(item: ProductModel.ColorQuantityModel) {
        this.items.add(item)
        notifyDataSetChanged()
    }


    /**
     * Update an item in specific position.
     */
    fun updateItem(position: Int, item: ProductModel.ColorQuantityModel) {
        this.items.removeAt(position)
        this.items.add(position, item)
        notifyItemChanged(position)
    }

    /**
     * Delete an item in specific position.
     */
    fun deleteItem(position: Int) {
        this.items.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorQuantityHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.color_quantity_item, parent, false)
        return ColorQuantityHolder(view)
    }

    override fun onBindViewHolder(holder: ColorQuantityHolder, position: Int) {
        holder.bindView(items[position], position, clickListener)
    }

    override fun getItemCount(): Int = items.size

    fun getItems(): ArrayList<ProductModel.ColorQuantityModel> =
        this.items

    /**
     * [RecyclerView.ViewHolder] class.
     */
    class ColorQuantityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardViewColor: CardView = itemView.findViewById(R.id.cardViewColor)
        private val textViewQuantity: AppCompatTextView = itemView.findViewById(R.id.textViewQuantity)
        private val textViewName: AppCompatTextView = itemView.findViewById(R.id.textViewName)
        private val imageViewTransparentEffect: AppCompatImageView = itemView.findViewById(R.id.imageViewTransparentEffect)

        fun bindView(colorQuantity: ProductModel.ColorQuantityModel, position: Int, clickListener: OnColorQuantityClickListener) {
            if (colorQuantity.value.toInt() != 0) {
                cardViewColor.setCardBackgroundColor(colorQuantity.value.toInt())
                imageViewTransparentEffect.visibility = View.GONE
            } else {
                cardViewColor.setCardBackgroundColor(-1)
                imageViewTransparentEffect.visibility = View.VISIBLE
            }
            cardViewColor.setCardBackgroundColor(if (colorQuantity.value != "0") colorQuantity.value.toInt() else -1)
            textViewQuantity.text = colorQuantity.quantity.toString()
            textViewName.text = colorQuantity.name
            itemView.setOnClickListener {
                clickListener.onClick(position, colorQuantity)
            }
        }

    }
}