package com.cmdv.domain.repositories

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.ProductModel

interface ProductRepository {

    fun updateProduct(id: Int, product: ProductModel): MutableLiveData<ProductModel>

    fun createProduct(
        name: String,
        description: String,
        productType: String,
        costPrice: String,
        originalPrice: String,
        sellingPrice: String,
        quantity: Int,
        colorQuantities: ArrayList<Pair<String, Int>>,
        lowBarrier: Int,
        tags: List<String>
    ): MutableLiveData<LiveDataStatusWrapper<ProductModel?>>

    fun getProducts(productsMutableLiveData: MutableLiveData<LiveDataStatusWrapper<List<ProductModel>>>)

    fun getProductTypes(mutableLiveData: MutableLiveData<LiveDataStatusWrapper<ArrayList<String>>>)

}