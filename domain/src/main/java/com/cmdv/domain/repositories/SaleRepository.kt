package com.cmdv.domain.repositories

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.SaleModel

interface SaleRepository {

    fun createSale(liveData:  MutableLiveData<LiveDataStatusWrapper<SaleModel>>, sale: SaleModel)

    fun getSales(liveData:  MutableLiveData<LiveDataStatusWrapper<List<SaleModel>>>)

}