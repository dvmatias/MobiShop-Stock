package com.cmdv.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
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

    override suspend fun getShopCartById(id: Long): ShopCartModel =
        ShopCartDatabaseMapper().transformEntityToModel(shopCartDAO.getById(id.toInt()))

    override fun getAllOpenShopCarts(): LiveData<List<ShopCartModel>> {
        return Transformations.map(
            shopCartDAO.getAllOpenShopCarts()
        ) { list ->
            return@map list?.map {
                ShopCartDatabaseMapper().transformEntityToModel(it)
            } ?: listOf()
        }
    }

    override suspend fun getOpenShopCart(): ShopCartModel? {
        shopCartDAO.getOpenShopCarts().forEach {
            if (!it.isClosed!!) {
                return ShopCartDatabaseMapper().transformEntityToModel(it)
            }
        }
        return null
    }

    override suspend fun addProduct(shopCartId: Long, product: ShopCartModel.ShopCartProductModel) {
        val shopCart: ShopCartModel = getShopCartById(shopCartId)
        var alreadyContainProduct = false
        var index = -1
        for (i in 0 until shopCart.products.size) {
            val p = shopCart.products[i]
            if (p.code == product.code) {
                index = i
                alreadyContainProduct = true
                break
            }
        }
        if (alreadyContainProduct) {
            shopCart.products.removeAt(index)
            shopCart.products.add(index, product)
        } else {
            shopCart.products.add(product)
        }
        // TODO update date of update.
        updateShopCart(shopCart)
    }

    override suspend fun deleteAll() {
        shopCartDAO.deleteAll()
    }

    override suspend fun getDataCount(): Int =
        shopCartDAO.getDataCount()

}