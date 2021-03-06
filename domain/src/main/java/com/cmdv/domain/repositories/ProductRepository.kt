package com.cmdv.domain.repositories

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.ProductModel
import com.cmdv.domain.models.ShopCartModel

interface ProductRepository {

    fun updateProduct(
        productMutableLiveData: MutableLiveData<LiveDataStatusWrapper<ProductModel>>,
        productUpdate: ProductModel
    )

    fun createProduct(
        name: String,
        description: String,
        productType: String,
        costPrice: String,
        originalPrice: String,
        sellingPrice: String,
        colorQuantities: ArrayList<ProductModel.ColorQuantityModel>,
        lowBarrier: Int,
        tags: List<String>
    ): MutableLiveData<LiveDataStatusWrapper<ProductModel?>>

    fun getProducts(productsMutableLiveData: MutableLiveData<LiveDataStatusWrapper<List<ProductModel>>>)

    fun getProductTypes(mutableLiveData: MutableLiveData<LiveDataStatusWrapper<ArrayList<String>>>)

    suspend fun saleProductsInShopCart(shopCart: ShopCartModel)

    fun searchProducts(
        _mutableLiveDataFilteredProduct: MutableLiveData<LiveDataStatusWrapper<List<ProductModel>>>,
        queryString: String
    )

    fun markProductInactive(productId: Long)

}