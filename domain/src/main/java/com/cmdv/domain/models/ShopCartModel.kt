package com.cmdv.domain.models

enum class ShopCartStatus(val status: String) {
    OPEN("open"),
    CLOSED("closed"),
    PENDING("pending")
}

data class ShopCartModel(
    val products: ArrayList<Any>,
    val total: Double,
    val status: ShopCartStatus
)