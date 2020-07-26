package com.cmdv.domain.repositories

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.datasources.firebase.SaleStoreListener
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.SaleModel
import com.cmdv.domain.models.ShopCartModel

interface SaleRepository {

    fun createSale(shopCartModel: ShopCartModel, sale: SaleModel, listener: SaleStoreListener)

    fun getSales(liveData:  MutableLiveData<LiveDataStatusWrapper<List<SaleModel>>>)

}