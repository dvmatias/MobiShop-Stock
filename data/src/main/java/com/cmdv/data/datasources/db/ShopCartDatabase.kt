package com.cmdv.data.datasources.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cmdv.data.entities.db.ShopCartDatabaseEntity

@Database(
    entities = [ShopCartDatabaseEntity::class],
    version = 2
)
abstract class ShopCartDatabase : RoomDatabase() {

    abstract val shopCartDAO: ShopCartDAO

    companion object {
        @Volatile
        private var INSTANCE: ShopCartDatabase? = null
        fun getInstance(context: Context): ShopCartDatabase {
            synchronized(this) {
                var instance: ShopCartDatabase? = INSTANCE
                if (instance == null) {
                    instance =
                        Room.databaseBuilder(
                            context.applicationContext,
                            ShopCartDatabase::class.java,
                            "shop_cart_database"
                        )
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }
        }
    }

}