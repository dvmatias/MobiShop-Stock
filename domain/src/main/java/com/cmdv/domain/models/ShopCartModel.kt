package com.cmdv.domain.models

enum class ShopCartStatus(val status: String) {
    OPEN("open"),
    CLOSED("closed")
}

data class ShopCartModel(
    val id: Long,
    val name: String,
    val date: DateModel,
    val status: ShopCartStatus,
    val products: List<ShopCartProductModel>
) {

    data class DateModel(
        val createdDate: String,
        val updatedDate: String,
        val closedDate: String
    )

    data class ShopCartProductModel(
        val code: String,
        val name: String,
        val price: Double,
        val imageName: String,
        val colorQuantity: List<ShopCartProductColorQuantityDatabaseModel>?
    )

    data class ShopCartProductColorQuantityDatabaseModel(
        val colorValue: String,
        val colorQuantity: Int
    )

}