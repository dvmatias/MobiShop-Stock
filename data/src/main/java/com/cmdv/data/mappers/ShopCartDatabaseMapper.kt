package com.cmdv.data.mappers

import com.cmdv.data.entities.db.ShopCartDatabaseEntity
import com.cmdv.data.entities.db.ShopCartDatabaseEntity.*
import com.cmdv.data.entities.db.ShopCartDatabaseEntity.ShopCartProductDatabaseEntity.*
import com.cmdv.domain.mapper.BaseMapper
import com.cmdv.domain.models.ShopCartModel
import com.cmdv.domain.models.ShopCartModel.*
import com.cmdv.domain.models.ShopCartStatus

private const val DEFAULT_ENTITY_ID = 0
private const val DEFAULT_STRING = ""
private const val DEFAULT_DOUBLE = 0.0
private const val DEFAULT_INT = 0
private const val DEFAULT_ENTITY_IS_CLOSED = false

class ShopCartDatabaseMapper : BaseMapper<ShopCartDatabaseEntity, ShopCartModel>() {

    override fun transformModelToEntity(m: ShopCartModel): ShopCartDatabaseEntity {
        val id: Int = m.id.toInt()
        val name: String? = m.name
        val isClosed: Boolean? = m.status == ShopCartStatus.CLOSED
        val date: ShopCartDateDatabaseEntity? = transformDateModelToEntity(m.date)
        val products: ArrayList<ShopCartProductDatabaseEntity>? = transformProductsModelToEntity(m.products)
        val discount: Double? = m.discount.toDouble()
        val subtotal: Double? = m.subtotal.toDouble()

        return ShopCartDatabaseEntity(id, name, isClosed, date, products, discount, subtotal)
    }

    private fun transformDateModelToEntity(dateModel: DateModel) =
        ShopCartDateDatabaseEntity(
            dateModel.createdDate,
            dateModel.updatedDate,
            dateModel.closedDate
        )

    private fun transformProductsModelToEntity(
        productsModel: ArrayList<ShopCartProductModel>
    ): ArrayList<ShopCartProductDatabaseEntity>? {
        val productsEntity: ArrayList<ShopCartProductDatabaseEntity> = arrayListOf()
        productsModel.forEach {
            productsEntity.add(
                ShopCartProductDatabaseEntity(
                    it.code,
                    it.name,
                    it.price.toDouble(),
                    it.imageName,
                    transformColoQuantityModelToEntity(it.colorQuantity)
                )
            )
        }
        return productsEntity
    }

    private fun transformColoQuantityModelToEntity(
        colorQuantityModel: List<ShopCartProductColorQuantityDatabaseModel>
    ) : ArrayList<ShopCartProductColorQuantityDatabaseEntity>? {
        val colorQuantityEntity: ArrayList<ShopCartProductColorQuantityDatabaseEntity> = arrayListOf()
        colorQuantityModel.forEach {
            colorQuantityEntity.add(
                ShopCartProductColorQuantityDatabaseEntity(
                    it.colorValue,
                    it.colorQuantity
                )
            )
        }

        return colorQuantityEntity
    }

    override fun transformEntityToModel(e: ShopCartDatabaseEntity): ShopCartModel {
        val id: Long = e.id.toLong()
        val name: String = e.name ?: DEFAULT_STRING
        val date: DateModel =
            DateModel(
                e.date?.createdDate ?: DEFAULT_STRING,
                e.date?.updatedDate ?: DEFAULT_STRING,
                e.date?.closedDate ?: DEFAULT_STRING
            )
        val status: ShopCartStatus = if (e.isClosed!!) ShopCartStatus.CLOSED else ShopCartStatus.OPEN
        val products: ArrayList<ShopCartProductModel> = transformProductsEntityToModel(e.products)
        val discount: String = e.discount.toString()
        val subtotal: String = e.subtotal.toString()

        return ShopCartModel(id, name, date, status, products, discount, subtotal)
    }

    private fun transformProductsEntityToModel(
        products: ArrayList<ShopCartProductDatabaseEntity>?
    ): ArrayList<ShopCartProductModel> {
        val model = arrayListOf<ShopCartProductModel>()
        products?.forEach {
            model.add(
                ShopCartProductModel(
                    it.code ?: DEFAULT_STRING,
                    it.name ?: DEFAULT_STRING,
                    it.price?.toString() ?: DEFAULT_STRING,
                    it.imageName ?: DEFAULT_STRING,
                    transformColoQuantityEntityToModel(it.colorQuantity)
                )
            )
        }

        return model
    }
    private fun transformColoQuantityEntityToModel(
        colorQuantity: ArrayList<ShopCartProductColorQuantityDatabaseEntity>?
    ): List<ShopCartProductColorQuantityDatabaseModel> =
        colorQuantity?.map {
            ShopCartProductColorQuantityDatabaseModel(
                it.colorValue ?: DEFAULT_STRING,
                it.colorQuantity ?: DEFAULT_INT
            )
        } ?: listOf()

    fun getNewShopCart(name: String, createdDate: String): ShopCartDatabaseEntity =
        ShopCartDatabaseEntity(
            DEFAULT_ENTITY_ID,
            name,
            DEFAULT_ENTITY_IS_CLOSED,
            ShopCartDateDatabaseEntity(
                createdDate,
                createdDate,
                DEFAULT_STRING
            ),
            arrayListOf(),
            DEFAULT_DOUBLE,
            DEFAULT_DOUBLE
        )
}