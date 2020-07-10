package com.cmdv.components.colorpicker

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.recyclerview.widget.GridLayoutManager
import com.cmdv.components.R
import com.cmdv.core.helpers.DisplayHelper
import kotlinx.android.synthetic.main.component_colo_picker.*

private const val DIALOG_WIDTH_DISPLAY_PERCENTAGE = 0.8

internal class ComponentColorPickerDialog(context: Context) : Dialog(context) {

    private var listener: ColorSelectedListener? = null

    private var currentSelectedPosition: Int = -1

    private var oldSelectedPosition: Int = -1

    private lateinit var adapter: RecyclerColorsAdapter

    private var colorSelected = Color.TRANSPARENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.component_colo_picker)
        window?.setLayout(
            (DisplayHelper.getDisplayWidthPx() * DIALOG_WIDTH_DISPLAY_PERCENTAGE).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setupRecycler()
        buttonNegative.setOnClickListener { dismiss() }
        buttonPositive.setOnClickListener {
            listener?.onColorSelected(colorSelected)
            dismiss()
        }
    }

    private fun setupRecycler() {
        adapter = RecyclerColorsAdapter(context, clickListener, null)
        recyclerViewColors.let {
            it.layoutManager = GridLayoutManager(context, 4)
            it.adapter = adapter
        }
    }

    private val clickListener = object : RecyclerColorsAdapter.OnColorClick {
        override fun onClick(position: Int, color: Int) {
            oldSelectedPosition = currentSelectedPosition
            currentSelectedPosition = position
            when (position) {
                oldSelectedPosition -> { }
                else -> {
                    adapter.updateSelected(currentSelectedPosition, oldSelectedPosition)
                    colorSelected = adapter.colorSelected
                    recyclerViewColors.post { adapter.notifyDataSetChanged() }
                }
            }
        }
    }

    internal fun setListener(listener: ColorSelectedListener) {
        this.listener = listener
    }

}