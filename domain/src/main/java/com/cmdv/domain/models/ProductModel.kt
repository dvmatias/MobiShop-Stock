package com.cmdv.domain.models

data class ProductModel(
    val code: String,
    val id: Long,
    val productType: String,
    val name: String,
    val description: String,
    val model: String,
    val imageName: String,
    val price: PriceModel,
    val quantity: QuantityModel,
    val tags: List<String>,
    val date: DateModel
)

data class PriceModel(
    val costPrice: String,
    val originalPrice: String,
    val sellingPrice: String
)

data class QuantityModel(
    val initial: Int,
    val available: Int,
    val sold: Int,
    val lowBarrier: Int
)

data class DateModel(
    val createdDate: String,
    val updatedDate: String
)