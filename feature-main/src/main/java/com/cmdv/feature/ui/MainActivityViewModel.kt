package com.cmdv.feature.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmdv.domain.models.ProductModel
import com.cmdv.domain.repositories.ProductRepository

class MainActivityViewModel(private val productRepository: ProductRepository) : ViewModel() {

    var productList = MutableLiveData<List<ProductModel>>()
        private set

    fun getProducts() {
        productList = productRepository.getProducts()
    }

}