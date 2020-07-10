package com.cmdv.components.colorpicker

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.cmdv.components.R
import kotlinx.android.synthetic.main.color_picker_button_component.view.*

class ComponentColorPickerButton : ConstraintLayout {

    private lateinit var d: ComponentColorPickerDialog

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView(context)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        initView(context)
    }

    /**
     * Init view.
     */
    private fun initView(context: Context) {
        View.inflate(context, R.layout.color_picker_button_component, this)

        d = ComponentColorPickerDialog(context)
        this.setOnClickListener {
            d.apply {
                setListener(colorSelectedListener)
                show()
            }
        }
    }

    fun setColorSelectedListener(colorSelectedListener: ColorSelectedListener) {
        this.listener = colorSelectedListener
    }

    private lateinit var listener: ColorSelectedListener

    private val colorSelectedListener = object : ColorSelectedListener {
        override fun onColorSelected(color: Int) {
            listener.onColorSelected(color)
            if (color != Color.TRANSPARENT) {
                cardViewColor.setCardBackgroundColor(ColorStateList.valueOf(color))
                imageViewTransparentEffect.visibility = View.GONE
            } else {
                imageViewTransparentEffect.visibility = View.VISIBLE
            }
        }

        override fun onNothingSelected() {
            listener.onNothingSelected()
        }
    }
}