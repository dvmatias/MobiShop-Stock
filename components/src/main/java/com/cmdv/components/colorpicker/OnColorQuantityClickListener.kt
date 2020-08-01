package com.cmdv.components.colorpicker

import com.cmdv.domain.models.ProductModel

/**
 * Interface to communicate click events on [RecyclerColoQuantityAdapter] items
 * to [ComponentColorQuantity] class in order to open a [ComponentColorQuantityPickerDialog] dialog.
 */
interface OnColorQuantityClickListener {
    fun onClick(position: Int, colorQuantity: ProductModel.ColorQuantityModel)
}