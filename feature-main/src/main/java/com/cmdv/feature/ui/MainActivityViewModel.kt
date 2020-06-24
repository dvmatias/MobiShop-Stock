package com.cmdv.feature.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.ProductModel
import com.cmdv.domain.repositories.ProductRepository

class MainActivityViewModel(private val productRepository: ProductRepository) : ViewModel() {

    // Product list
    private val _products = MutableLiveData<LiveDataStatusWrapper<List<ProductModel>>>()
    val products: LiveData<LiveDataStatusWrapper<List<ProductModel>>>
        get() = _products

    fun getProducts() {
        productRepository.getProducts(_products)
    }

}