package com.cmdv.data.mappers

import com.cmdv.data.PriceEntity
import com.cmdv.data.ProductFirebaseEntity
import com.cmdv.data.QuantityEntity
import com.cmdv.domain.mapper.BaseMapper
import com.cmdv.domain.models.PriceModel
import com.cmdv.domain.models.ProductModel
import com.cmdv.domain.models.QuantityModel

class ProductFirebaseMapper : BaseMapper<ProductFirebaseEntity, ProductModel>() {

    override fun transformEntityToModel(e: ProductFirebaseEntity): ProductModel =
        ProductModel(
            e.code ?: "",
            e.id ?: -1L,
            e.name ?: "",
            e.model ?: "",
            e.imageName ?: "",
            PriceModel(
                e.price?.costPrice ?: "",
                e.price?.originalPrice ?: "",
                e.price?.sellingPrice ?: ""
            ),
            QuantityModel(
                e.quantity?.initial ?: 0,
                e.quantity?.available ?: 0,
                e.quantity?.sold ?: 0
            ),
            listOf() // TODO transform entity to model tags
        )

    override fun transformModelToEntity(m: ProductModel): ProductFirebaseEntity =
        ProductFirebaseEntity(
            m.code,
            m.id,
            m.name,
            m.model,
            m.imageName,
            PriceEntity(m.price.costPrice, m.price.originalPrice, m.price.sellingPrice),
            QuantityEntity(m.quantity.initial, m.quantity.available, m.quantity.sold),
            transformTagsModelToEntity(m.tags)
        )

    private fun transformTagsModelToEntity(tags: List<String>): List<Map<String, String>> =
        tags.map {
            mapOf("_" to it)
        }

}