package com.cmdv.domain.models

data class ProductModel(
    val code: String,
    val id: Long,
    val isActive: Boolean,
    val productType: String,
    val name: String,
    val description: String,
    val model: String,
    val imageName: String,
    val price: PriceModel,
    val quantity: QuantityModel,
    val tags: List<String>,
    val date: DateModel
) {

    data class PriceModel(
        val costPrice: String,
        val originalPrice: String,
        val sellingPrice: String
    )

    data class QuantityModel(
        val sold: Int,
        val lowBarrier: Int,
        val colorQuantities: ArrayList<ColorQuantityModel>
    )

    data class ColorQuantityModel(
        val name: String,
        val value: String,
        val quantity: Int
    )

    data class DateModel(
        val createdDate: String,
        val updatedDate: String
    )

}