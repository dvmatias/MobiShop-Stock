package com.cmdv.domain.repositories

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.ProductModel

interface ProductRepository {

    fun updateProduct(id: Int, product: ProductModel): MutableLiveData<ProductModel>

    fun createProduct(
        name: String,
        productType: String,
        costPrice: String,
        originalPrice: String,
        sellingPrice: String,
        quantity: Int,
        tags: List<String>
    ): MutableLiveData<LiveDataStatusWrapper<ProductModel?>>

    fun getProducts(): MutableLiveData<List<ProductModel>>

    fun getProductTypes(mutableLiveData: MutableLiveData<LiveDataStatusWrapper<ArrayList<String>>>)

}