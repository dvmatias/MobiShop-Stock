package com.cmdv.data.mappers

import com.cmdv.data.entities.*
import com.cmdv.domain.mapper.BaseMapper
import com.cmdv.domain.models.*

class ProductFirebaseMapper : BaseMapper<ProductFirebaseEntity, ProductModel>() {

    override fun transformEntityToModel(e: ProductFirebaseEntity): ProductModel =
        ProductModel(
            e.code ?: "",
            e.id ?: -1L,
            e.productType ?: "",
            e.name ?: "",
            e.description ?: "",
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
                e.quantity?.sold ?: 0,
                e.quantity?.lowBarrier ?: 5,
                arrayListOf() // TODO
            ),
            e.tags?.map { it.values.toString() } ?: listOf(),
            DateModel(
                e.date?.createdDate ?: "",
                e.date?.updatedDate ?: ""
            )
        )

    override fun transformModelToEntity(m: ProductModel): ProductFirebaseEntity =
        ProductFirebaseEntity(
            m.code,
            m.id,
            m.productType,
            m.name,
            m.description,
            m.model,
            m.imageName,
            PriceEntity(m.price.costPrice, m.price.originalPrice, m.price.sellingPrice),
            QuantityEntity(
                m.quantity.initial,
                m.quantity.available,
                m.quantity.sold,
                m.quantity.lowBarrier,
                transformColorQuantitiesModelToEntity(m.quantity.colorQuantities)
            ),
            transformTagsModelToEntity(m.tags),
            DateEntity(m.date.createdDate, m.date.updatedDate)
        )

    private fun transformColorQuantitiesModelToEntity(colorQuantities: ArrayList<ColorQuantityModel>): List<ColorQuantityEntity> =
        colorQuantities.map {
            ColorQuantityEntity(
                it.name,
                it.value,
                it.quantity
            )
        }

    private fun transformTagsModelToEntity(tags: List<String>): List<Map<String, String>> =
        tags.map {
            mapOf("_" to it)
        }

}