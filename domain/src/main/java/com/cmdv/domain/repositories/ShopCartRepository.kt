package com.cmdv.domain.repositories

import androidx.lifecycle.LiveData
import com.cmdv.domain.models.ShopCartModel

interface ShopCartRepository {

    suspend fun insertShopCart(name: String, createdDate: String): Long

    suspend fun updateShopCart(shopCartModel: ShopCartModel)

    suspend fun deleteShopCart(shopCartModel: ShopCartModel)

    suspend fun getShopCartById(id: Long): ShopCartModel

    fun getAllOpenShopCarts(): LiveData<List<ShopCartModel>>

    suspend fun getOpenShopCart(): ShopCartModel?

    suspend fun addProduct(shopCartId: Long, product: ShopCartModel.ShopCartProductModel)

    suspend fun deleteAll()

    suspend fun getDataCount(): Int

}