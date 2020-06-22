package com.cmdv.domain.repositories

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.models.ProductModel

interface ProductRepository {

    fun updateProduct(id: Int, product: ProductModel): MutableLiveData<ProductModel>

    fun createProduct(
        name: String,
        costPrice: String,
        originalPrice: String,
        sellingPrice: String,
        quantity: Int,
        tags: List<String>
    ): MutableLiveData<ProductModel>

    fun getProducts(): MutableLiveData<List<ProductModel>>

}