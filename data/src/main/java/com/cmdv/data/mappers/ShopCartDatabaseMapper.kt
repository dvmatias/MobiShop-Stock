package com.cmdv.data.mappers

import com.cmdv.data.entities.db.ShopCartDatabaseEntity
import com.cmdv.domain.mapper.BaseMapper
import com.cmdv.domain.models.ShopCartModel

class ShopCartDatabaseMapper : BaseMapper<ShopCartDatabaseEntity, ShopCartModel>() {

    override fun transformModelToEntity(m: ShopCartModel): ShopCartDatabaseEntity {
        return super.transformModelToEntity(m)
    }

    override fun transformEntityToModel(e: ShopCartDatabaseEntity): ShopCartModel {
        return super.transformEntityToModel(e)
    }

    fun getNewShopCart(name: String, createdDate: String): ShopCartDatabaseEntity =
        ShopCartDatabaseEntity(
            0,
            name,
            ShopCartDatabaseEntity.ShopCartDateDatabaseEntity(
                createdDate,
                createdDate,
                null
            ),
            null,
            null,
            null
        )
}