package com.cmdv.data.repositories

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.models.ProductModel
import com.cmdv.domain.repositories.ProductRepository

class ProductRepositoryImpl: ProductRepository {

    override fun getProducts(): MutableLiveData<List<ProductModel>> {
        TODO("Not yet implemented")
    }

}