package com.cmdv.feature

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.ProductModel
import com.cmdv.domain.repositories.ProductRepository

class SearchActivityViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _mutableLiveDataFilteredProduct = MutableLiveData<LiveDataStatusWrapper<List<ProductModel>>>()
    val liveDataFilteredProducts: LiveData<LiveDataStatusWrapper<List<ProductModel>>>
        get() = _mutableLiveDataFilteredProduct

    fun searchProducts(queryString: String) {
        productRepository.searchProducts(_mutableLiveDataFilteredProduct, queryString)
    }
}