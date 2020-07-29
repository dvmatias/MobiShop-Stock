package com.cmdv.data.datasources.db

import androidx.lifecycle.LiveData
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

    @Query("SELECT * FROM shop_cart_table WHERE shop_cart_id=:id ")
    suspend fun getById(id: Int): ShopCartDatabaseEntity

    @Query("SELECT * FROM shop_cart_table")
    fun getAllOpenShopCarts(): LiveData<List<ShopCartDatabaseEntity>>

    @Query("DELETE FROM shop_cart_table")
    suspend fun deleteAll()

    @Query("SELECT COUNT(shop_cart_id) FROM shop_cart_table")
    suspend fun getDataCount(): Int

    @Query("SELECT * FROM shop_cart_table")
    suspend fun getOpenShopCarts(): List<ShopCartDatabaseEntity>

}