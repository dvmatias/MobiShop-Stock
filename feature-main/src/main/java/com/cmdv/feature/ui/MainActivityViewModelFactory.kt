package com.cmdv.feature.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmdv.data.repositories.ProductRepositoryImpl
import com.cmdv.domain.repositories.ProductRepository

class MainActivityViewModelFactory(private val productRepository: ProductRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MainActivityViewModel(productRepository) as T

}