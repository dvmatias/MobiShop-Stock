package com.cmdv.data.mappers

import com.cmdv.data.entities.firebase.*
import com.cmdv.domain.mapper.BaseMapper
import com.cmdv.domain.models.ProductModel

class ProductFirebaseMapper : BaseMapper<ProductFirebaseEntity, ProductModel>() {

    override fun transformEntityToModel(e: ProductFirebaseEntity): ProductModel =
        ProductModel(
            e.code ?: "",
            e.id ?: -1L,
            e.active ?: false,
            e.productType ?: "",
            e.name ?: "",
            e.description ?: "",
            e.model ?: "",
            e.imageName ?: "",
            ProductModel.PriceModel(
                e.price?.costPrice ?: "",
                e.price?.originalPrice ?: "",
                e.price?.sellingPrice ?: ""
            ),
            ProductModel.QuantityModel(
                e.quantity?.sold ?: 0,
                e.quantity?.lowBarrier ?: 5,
                transformColorQuantitiesEntityToModel(e.quantity?.colorQuantities)
            ),
            e.tags?.map { it.values.toString() } ?: listOf(),
            ProductModel.DateModel(
                e.date?.createdDate ?: "",
                e.date?.updatedDate ?: ""
            )
        )

    override fun transformModelToEntity(m: ProductModel): ProductFirebaseEntity =
        ProductFirebaseEntity(
            m.code,
            m.id,
            m.isActive,
            m.productType,
            m.name,
            m.description,
            m.model,
            m.imageName,
            PriceEntity(m.price.costPrice, m.price.originalPrice, m.price.sellingPrice),
            QuantityEntity(
                m.quantity.sold,
                m.quantity.lowBarrier,
                transformColorQuantitiesModelToEntity(m.quantity.colorQuantities)
            ),
            transformTagsModelToEntity(m.tags),
            DateEntity(m.date.createdDate, m.date.updatedDate)
        )

    private fun transformColorQuantitiesModelToEntity(colorQuantities: ArrayList<ProductModel.ColorQuantityModel>): List<ColorQuantityEntity> =
        colorQuantities.map {
            ColorQuantityEntity(
                it.name,
                it.value,
                it.quantity
            )
        }

    private fun transformColorQuantitiesEntityToModel(colorQuantities: List<ColorQuantityEntity>?): ArrayList<ProductModel.ColorQuantityModel> {
        val response: ArrayList<ProductModel.ColorQuantityModel> = arrayListOf()
        if (colorQuantities == null) return response

        for (e in colorQuantities) {
            response.add(
                ProductModel.ColorQuantityModel(
                    e.name ?: "",
                    e.value ?: "",
                    e.quantity ?: 0
                )
            )
        }

        return response
    }

    private fun transformTagsModelToEntity(tags: List<String>): List<Map<String, String>> =
        tags.map {
            mapOf("_" to it)
        }

}