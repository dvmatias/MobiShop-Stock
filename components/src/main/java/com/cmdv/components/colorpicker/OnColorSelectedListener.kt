package com.cmdv.components.colorpicker

/**
 * Interface to callback when a color has been selected in [RecyclerColorsAdapter].
 * This callback updates the position of the color selected in order to update the
 * item view and mark selected the proper one.
 */
interface OnColorSelectedListener {
    fun onColorSelected(position: Int)
}