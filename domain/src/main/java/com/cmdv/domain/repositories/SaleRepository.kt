package com.cmdv.domain.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.SaleModel
import com.cmdv.domain.models.ShopCartModel

interface SaleRepository {

    fun createSale(sale: SaleModel): MutableLiveData<LiveDataStatusWrapper<SaleModel>>

}