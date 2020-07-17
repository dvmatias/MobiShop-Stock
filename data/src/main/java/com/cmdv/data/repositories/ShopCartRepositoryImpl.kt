package com.cmdv.data.repositories

import com.cmdv.data.datasources.db.ShopCartDAO
import com.cmdv.data.mappers.ShopCartDatabaseMapper
import com.cmdv.domain.models.ShopCartModel
import com.cmdv.domain.repositories.ShopCartRepository

// TODO Add FB data source
class ShopCartRepositoryImpl(
    private val shopCartDAO: ShopCartDAO
) : ShopCartRepository {

    override suspend fun insertShopCart(name: String, createdDate: String): Long =
        shopCartDAO.insertShopCart(ShopCartDatabaseMapper().getNewShopCart(name, createdDate))

    override suspend fun updateShopCart(shopCartModel: ShopCartModel) =
        shopCartDAO.updateShopCart(ShopCartDatabaseMapper().transformModelToEntity(shopCartModel))

    override suspend fun deleteShopCart(shopCartModel: ShopCartModel) =
        shopCartDAO.deleteShopCart(ShopCartDatabaseMapper().transformModelToEntity(shopCartModel))

    override suspend fun getAllShopCarts(): List<ShopCartModel> =
        shopCartDAO.getAllShopCarts().map {
            ShopCartDatabaseMapper().transformEntityToModel(it)
        }

}