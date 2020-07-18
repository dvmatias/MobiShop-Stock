package com.cmdv.domain.repositories

import androidx.lifecycle.LiveData
import com.cmdv.domain.models.ShopCartModel

interface ShopCartRepository {

    suspend fun insertShopCart(name: String, createdDate: String): Long

    suspend fun updateShopCart(shopCartModel: ShopCartModel)

    suspend fun deleteShopCart(shopCartModel: ShopCartModel)

    fun getAllOpenShopCarts(): LiveData<List<ShopCartModel>>

    suspend fun deleteAll()

}