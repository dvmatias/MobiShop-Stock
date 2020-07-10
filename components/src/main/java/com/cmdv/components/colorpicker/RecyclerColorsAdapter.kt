package com.cmdv.components.colorpicker

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.components.R

class RecyclerColorsAdapter(
    private val context: Context,
    private val clickListener: OnColorClick,
    private val selectedColor: Int?
) : RecyclerView.Adapter<RecyclerColorsAdapter.ColorViewHolder>() {

    private var currentSelectedPosition: Int = -1

    private var oldSelectedPosition: Int = -1

    private var isViewInitialized: Boolean = false

    var colorSelected = Color.TRANSPARENT

    private val data: ArrayList<Pair<String, String>> =
        arrayListOf(
            "#00000000" to "Transparent",
            "#FFFFFF" to "Blanco",
            "#000000" to "Negro",
            "#CFCFC4" to "Gris",
            "#836953" to "Marrón",
            "#FF6961" to "Rojo",
            "#FFb347" to "Naranja",
            "#FDFD96" to "Amarillo",
            "#77DD77" to "Verde",
            "#779ECB" to "Azul",
            "#ACE7FF" to "Celeste",
            "#CCA9DD" to "Violeta",
            "#FFAABE" to "Rosa",
            "#FBC2AD" to "Salmón",
            "#FFDBAC" to "Piel",
            "#E1C699" to "Biege"
        )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder =
        ColorViewHolder(LayoutInflater.from(context).inflate(R.layout.color_picker_item, parent, false))

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bindView(clickListener, data[position], position, currentSelectedPosition)
        if (selectedColor == null && position == 0 && !isViewInitialized) {
            isViewInitialized = true
            holder.initialize()
        }
    }

    override fun getItemCount(): Int = data.size

    fun updateSelected(currentSelectedPosition: Int, oldSelectedPosition: Int) {
        this.currentSelectedPosition = currentSelectedPosition
        this.oldSelectedPosition = oldSelectedPosition
        this.colorSelected = Color.parseColor(data[currentSelectedPosition].first)
    }

    /**
     * View Holder.
     */
    class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardViewSelection: CardView = itemView.findViewById(R.id.cardViewSelection)
        private val cardViewColor: CardView = itemView.findViewById(R.id.cardViewColor)
        private val imageViewTransparentEffect: AppCompatImageView = itemView.findViewById(R.id.imageViewTransparentEffect)

        fun bindView(listener: OnColorClick, pairColor: Pair<String, String>, position: Int, currentPosition: Int) {
            val color: Int =
                if (pairColor.first.isNotEmpty())
                    Color.parseColor(pairColor.first)
                else
                    Color.TRANSPARENT
            if (position == 0) {
                imageViewTransparentEffect.visibility = View.VISIBLE
            }
            cardViewSelection.visibility = if (position == currentPosition) View.VISIBLE else View.GONE
            cardViewColor.setCardBackgroundColor(ColorStateList.valueOf(color))
            itemView.setOnClickListener {
                listener.onClick(position, color)
            }
        }

        fun initialize() {
            itemView.performClick()
        }

    }

    interface OnColorClick {
        fun onClick(position: Int, color: Int)
    }

}