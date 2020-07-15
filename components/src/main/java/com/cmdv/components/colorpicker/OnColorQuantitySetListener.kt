package com.cmdv.components.colorpicker

import com.cmdv.domain.models.ColorQuantityModel

/**
 * Interface to communicate [ComponentColorQuantityPickerDialog] class with
 * [ComponentColorQuantity] class.
 */
interface OnColorQuantitySetListener {
    fun onColorQuantityCreated(colorQuantity: ColorQuantityModel)
    fun onColorQuantityUpdated(position: Int, colorQuantity: ColorQuantityModel)
    fun onColorQuantityDeleted(position: Int)
}