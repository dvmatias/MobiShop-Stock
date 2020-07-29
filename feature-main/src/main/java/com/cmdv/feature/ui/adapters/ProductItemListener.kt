package com.cmdv.feature.ui.adapters

import android.view.View

interface ProductItemListener {

    fun onOverflowMenuButtonClick(position: Int, anchorView: View)
    fun onAddProductToShopCartButtonClick(position: Int)

}