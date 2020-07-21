package com.cmdv.data.mappers

import com.cmdv.data.entities.db.ShopCartDatabaseEntity
import com.cmdv.domain.mapper.BaseMapper
import com.cmdv.domain.models.ShopCartModel
import com.cmdv.domain.models.ShopCartStatus

private const val DEFAULT_ENTITY_ID = 0
private const val DEFAULT_STRING = ""
private const val DEFAULT_DOUBLE = 0.0
private const val DEFAULT_INT = 0
private const val DEFAULT_ENTITY_IS_CLOSED = false

class ShopCartDatabaseMapper : BaseMapper<ShopCartDatabaseEntity, ShopCartModel>() {

    override fun transformModelToEntity(m: ShopCartModel): ShopCartDatabaseEntity {
        return super.transformModelToEntity(m)
    }

    override fun transformEntityToModel(e: ShopCartDatabaseEntity): ShopCartModel {
        val id: Long = e.id.toLong()
        val name: String = e.name ?: DEFAULT_STRING
        val date: ShopCartModel.DateModel =
            ShopCartModel.DateModel(
                e.date?.createdDate ?: DEFAULT_STRING,
                e.date?.updatedDate ?: DEFAULT_STRING,
                e.date?.closedDate ?: DEFAULT_STRING
            )
        val status: ShopCartStatus = if (e.isClosed!!) ShopCartStatus.CLOSED else ShopCartStatus.OPEN
        val products: List<ShopCartModel.ShopCartProductModel> = transformProductsEntityToModel(e.products)

        return ShopCartModel(id, name, date, status, products)
    }

    private fun transformProductsEntityToModel(
        products: ArrayList<ShopCartDatabaseEntity.ShopCartProductDatabaseEntity>?
    ): List<ShopCartModel.ShopCartProductModel> =
        products?.map {
            ShopCartModel.ShopCartProductModel(
                it.code ?: DEFAULT_STRING,
                it.name ?: DEFAULT_STRING,
                it.price?.toString() ?: DEFAULT_STRING,
                it.imageName ?: DEFAULT_STRING,
                transformColoQuantityEntityToModel(it.colorQuantity)
            )
        } ?: listOf()

    private fun transformColoQuantityEntityToModel(
        colorQuantity: ArrayList<ShopCartDatabaseEntity.ShopCartProductDatabaseEntity.ShopCartProductColorQuantityDatabaseEntity>?
    ): List<ShopCartModel.ShopCartProductColorQuantityDatabaseModel> =
        colorQuantity?.map {
            ShopCartModel.ShopCartProductColorQuantityDatabaseModel(
                it.colorValue ?: DEFAULT_STRING,
                it.colorQuantity ?: DEFAULT_INT
            )
        } ?: listOf()

    fun getNewShopCart(name: String, createdDate: String): ShopCartDatabaseEntity =
        ShopCartDatabaseEntity(
            DEFAULT_ENTITY_ID,
            name,
            DEFAULT_ENTITY_IS_CLOSED,
            ShopCartDatabaseEntity.ShopCartDateDatabaseEntity(
                createdDate,
                createdDate,
                DEFAULT_STRING
            ),
            arrayListOf(),
            DEFAULT_DOUBLE,
            DEFAULT_DOUBLE
        )
}