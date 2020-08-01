package com.cmdv.core.helpers

import com.cmdv.domain.models.ProductModel
import java.util.ArrayList

class ProductQuantityHelper {

    companion object {

        fun getQuantityAvailable(colorQuantities: ArrayList<ProductModel.ColorQuantityModel>): Int =
            colorQuantities.map { it.quantity }.sum()

    }

}