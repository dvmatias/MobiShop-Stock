package com.cmdv.domain.datasources.firebase

import com.cmdv.domain.models.ShopCartModel

interface SaleStoreListener {

    fun onSuccess(shopCart: ShopCartModel)

    fun onFailure()

}