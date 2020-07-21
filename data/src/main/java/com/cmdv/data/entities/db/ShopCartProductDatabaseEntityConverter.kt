package com.cmdv.data.entities.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ShopCartProductDatabaseEntityConverter {

    @TypeConverter
    fun fromEntity(entity: ArrayList<ShopCartDatabaseEntity.ShopCartProductDatabaseEntity>?): String? {
        if (entity == null) return null

        val type: Type = object : TypeToken<ArrayList<ShopCartDatabaseEntity.ShopCartProductDatabaseEntity>?>() {}.type
        return Gson().toJson(entity, type)
    }

    @TypeConverter
    fun toEntity(json: String?): ArrayList<ShopCartDatabaseEntity.ShopCartProductDatabaseEntity>? {
        if (json == null) return null

        val type: Type = object : TypeToken<ArrayList<ShopCartDatabaseEntity.ShopCartProductDatabaseEntity>?>() {}.type
        return Gson().fromJson(json, type)
    }

}