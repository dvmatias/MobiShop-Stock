package com.cmdv.core.helpers

import com.cmdv.domain.models.ColorQuantityModel
import java.util.ArrayList

class ProductQuantityHelper {

    companion object {

        fun getQuantityAvailable(colorQuantities: ArrayList<ColorQuantityModel>): Int =
            colorQuantities.map { it.quantity }.sum()

    }

}