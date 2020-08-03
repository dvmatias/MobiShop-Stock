package com.cmdv.components.colorpicker

import com.cmdv.domain.models.ProductModel

/**
 * Interface to communicate [ComponentColorQuantityPickerDialog] class with
 * [ComponentColorQuantity] class.
 */
interface OnColorQuantitySetListener {
    fun onColorQuantityCreated(colorQuantity: ProductModel.ColorQuantityModel)
    fun onColorQuantityUpdated(position: Int, colorQuantity: ProductModel.ColorQuantityModel)
    fun onColorQuantityDeleted(position: Int)
}