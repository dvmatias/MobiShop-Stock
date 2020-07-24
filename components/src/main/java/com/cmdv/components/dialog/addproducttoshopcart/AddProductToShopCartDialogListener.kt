package com.cmdv.components.dialog.addproducttoshopcart

import com.cmdv.domain.models.ShopCartModel

interface AddProductToShopCartDialogListener {

    fun onAddProductToShopCartDialogPositiveClick(shopCartId: Long, product: ShopCartModel.ShopCartProductModel)

}
