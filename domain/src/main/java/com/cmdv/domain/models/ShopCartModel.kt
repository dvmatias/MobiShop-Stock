package com.cmdv.domain.models

enum class ShopCartStatus(val status: String) {
    OPEN("open"),
    CLOSED("closed"),
    PENDING("pending")
}

data class ShopCartModel(
    val products: ArrayList<Int>,
    val total: Double,
    val status: ShopCartStatus
)


/*

data class ShopCartDatabaseEntity(
    val id: Int,
    val name: String,
    val date: ShopCartDateDatabaseEntity,
    val products: ArrayList<ShopCartProductDatabaseEntity>,
    val discount: Double,
    val subtotal: Double
) {

    data class ShopCartDateDatabaseEntity(
        val createdDate: String,
        val updatedDate: String,
        val closedDate: String
    )

    data class ShopCartProductDatabaseEntity(
        val name: String,
        val price: Double,
        val code: String,
        val imageName: String,
        val colorQuantity: ArrayList<ShopCartProductColorQuantityDatabaseEntity>
    ) {

        data class ShopCartProductColorQuantityDatabaseEntity(
            val colorValue: String,
            val colorQuantity: Int
        )

    }

}

 */