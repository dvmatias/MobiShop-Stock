package com.cmdv.domain.repositories

import com.cmdv.domain.models.ShopCartModel

interface ShopCartRepository {

    suspend fun insertShopCart(name: String, createdDate: String): Long

    suspend fun updateShopCart(shopCartModel: ShopCartModel)

    suspend fun deleteShopCart(shopCartModel: ShopCartModel)

    suspend fun getAllShopCarts(): List<ShopCartModel>

}