package com.cmdv.data.entities.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_cart_table")
data class ShopCartDatabaseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "shop_cart_id")
    val id: Int,
    @ColumnInfo(name = "shop_cart_name")
    val name: String?,
    @ColumnInfo(name = "shop_cart_is_closed")
    val isClosed: Boolean?,
    @Embedded
    val date: ShopCartDateDatabaseEntity?,
    @Embedded
    val products: ArrayList<ShopCartProductDatabaseEntity>?,
    @ColumnInfo(name = "shop_cart_discount")
    val discount: Double?,
    @ColumnInfo(name = "shop_cart_subtotal")
    val subtotal: Double?
) {

    data class ShopCartDateDatabaseEntity(
        @ColumnInfo(name = "shop_cart_date_created")
        val createdDate: String?,
        @ColumnInfo(name = "shop_cart_date_updated")
        val updatedDate: String?,
        @ColumnInfo(name = "shop_cart_date_closed")
        val closedDate: String?
    )

    data class ShopCartProductDatabaseEntity(
        val code: String?,
        val name: String?,
        val price: Double?,
        val imageName: String?,
        val colorQuantity: ArrayList<ShopCartProductColorQuantityDatabaseEntity>?
    ) {

        data class ShopCartProductColorQuantityDatabaseEntity(
            val colorValue: String?,
            val colorQuantity: Int?
        )

    }

}