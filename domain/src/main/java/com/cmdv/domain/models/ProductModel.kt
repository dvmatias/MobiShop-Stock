package com.cmdv.domain.models

data class ProductModel (
    val code: String,
    val id: Long,
    val name: String,
    val model: String,
    val imageName: String,
    val price: PriceModel,
    val quantity: QuantityModel,
    val tags: List<String>
)

data class PriceModel(
    val costPrice: String,
    val originalPrice: String,
    val sellingPrice: String
)

data class QuantityModel(
    val initial: Int,
    val available: Int,
    val sold: Int
)