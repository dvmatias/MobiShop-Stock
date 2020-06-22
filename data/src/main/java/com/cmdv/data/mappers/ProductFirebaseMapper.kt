package com.cmdv.data.mappers

import com.cmdv.data.PriceEntity
import com.cmdv.data.ProductFirebaseEntity
import com.cmdv.data.QuantityEntity
import com.cmdv.domain.mapper.BaseMapper
import com.cmdv.domain.models.ProductModel

class ProductFirebaseMapper : BaseMapper<ProductFirebaseEntity, ProductModel>() {

    override fun transformEntityToModel(e: ProductFirebaseEntity): ProductModel {
        return super.transformEntityToModel(e)
    }

    override fun transformModelToEntity(m: ProductModel): ProductFirebaseEntity =
        ProductFirebaseEntity(
            m.code,
            m.id,
            m.name,
            m.model,
            m.imageName,
            PriceEntity(m.price.costPrice, m.price.originalPrice, m.price.sellingPrice),
            QuantityEntity(m.quantity.initial, m.quantity.available, m.quantity.sold),
            m.tags
        )


}