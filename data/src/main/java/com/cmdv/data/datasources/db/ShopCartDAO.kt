package com.cmdv.data.datasources.db

import androidx.room.*
import com.cmdv.data.entities.db.ShopCartDatabaseEntity

@Dao
interface ShopCartDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertShopCart(shopCart: ShopCartDatabaseEntity): Long

    @Update
    suspend fun updateShopCart(shopCart: ShopCartDatabaseEntity)

    @Delete
    suspend fun deleteShopCart(shopCart: ShopCartDatabaseEntity)

    @Query("SELECT * FROM shop_cart_table")
    suspend fun getAllShopCarts():List<ShopCartDatabaseEntity>

}