package com.cmdv.components.colorpicker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.components.R

class RecyclerColorsAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerColorsAdapter.ColorViewHolder>() {

    private var currentSelectedPosition = 0
    private var oldSelectedPosition = 0
    private var items: ArrayList<Int> = arrayListOf(
        ContextCompat.getColor(context, R.color.productColorTransparent),
        ContextCompat.getColor(context, R.color.productColorWhite),
        ContextCompat.getColor(context, R.color.productColorBlack),
        ContextCompat.getColor(context, R.color.productColorGrey),
        ContextCompat.getColor(context, R.color.productColorBrown),
        ContextCompat.getColor(context, R.color.productColorRed),
        ContextCompat.getColor(context, R.color.productColorOrange),
        ContextCompat.getColor(context, R.color.productColorYellow),
        ContextCompat.getColor(context, R.color.productColorGreen),
        ContextCompat.getColor(context, R.color.productColorBlue),
        ContextCompat.getColor(context, R.color.productColorLightBlue),
        ContextCompat.getColor(context, R.color.productColorViolet),
        ContextCompat.getColor(context, R.color.productColorPink),
        ContextCompat.getColor(context, R.color.productColorSalmon),
        ContextCompat.getColor(context, R.color.productColorSkin),
        ContextCompat.getColor(context, R.color.productColorBeige)
    )
    private var colorNames: Array<String> = context.resources.getStringArray(R.array.colorName)

    /**
     * [OnColorSelectedListener] implementation.
     */
    private val onColorSelectedListener = object : OnColorSelectedListener {
        override fun onColorSelected(position: Int) {
            oldSelectedPosition = currentSelectedPosition
            currentSelectedPosition = position
            notifyItemChanged(oldSelectedPosition)
            notifyItemChanged(currentSelectedPosition)
        }
    }

    fun setSelected(color: Int) {
        for (i in 0 until items.size) {
            if (items[i] == color) {
                currentSelectedPosition = i
                break
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder =
        ColorViewHolder(LayoutInflater.from(context).inflate(R.layout.color_picker_item, parent, false))

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val isSelected = currentSelectedPosition == position
        holder.bindView(items[position], position, isSelected, onColorSelectedListener)
    }

    override fun getItemCount(): Int = items.size

    /**
     * Return the value of the selected color.
     */
    fun getSelectedColorValue(): Int {
        for (position in 0 until items.size) {
            if (currentSelectedPosition == position)
                return items[position]
        }
        return 0
    }

    fun getSelectedColorName(): String {
        for (position in colorNames.indices) {
            if (currentSelectedPosition == position)
                return colorNames[position]
        }
        return ""
    }

    /**
     * View Holder.
     */
    class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardViewSelection: CardView = itemView.findViewById(R.id.cardViewSelection)
        private val cardViewColor: CardView = itemView.findViewById(R.id.cardViewColor)
        private val imageViewTransparentEffect: AppCompatImageView = itemView.findViewById(R.id.imageViewTransparentEffect)


        fun bindView(color: Int, position: Int, isSelected: Boolean, onColorSelectedListener: OnColorSelectedListener) {
            // Transparent
            if (color == 0) {
                imageViewTransparentEffect.visibility = View.VISIBLE
            } else {
                imageViewTransparentEffect.visibility = View.GONE
                cardViewColor.setCardBackgroundColor(color)
            }
            cardViewSelection.visibility = if (isSelected) View.VISIBLE else View.GONE
            itemView.setOnClickListener {
                onColorSelectedListener.onColorSelected(position)
            }
        }
    }

}