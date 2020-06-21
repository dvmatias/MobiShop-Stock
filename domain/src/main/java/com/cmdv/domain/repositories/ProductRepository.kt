package com.cmdv.domain.repositories

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.models.ProductModel

interface ProductRepository {

    fun getProducts(): MutableLiveData<List<ProductModel>>

}