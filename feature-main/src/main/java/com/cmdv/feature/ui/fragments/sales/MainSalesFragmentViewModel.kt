package com.cmdv.feature.ui.fragments.sales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.SaleModel
import com.cmdv.domain.repositories.SaleRepository

class MainSalesFragmentViewModel(
    private val saleRepository: SaleRepository
) : ViewModel() {

    // Sales list
    private val _sales = MutableLiveData<LiveDataStatusWrapper<List<SaleModel>>>()
    val sales: LiveData<LiveDataStatusWrapper<List<SaleModel>>>
        get() = _sales

    fun getSales() {
        saleRepository.getSales(_sales)
    }

}
