package com.cmdv.data.entities.db

import androidx.room.TypeConverter
import com.cmdv.data.entities.db.ShopCartDatabaseEntity.ShopCartDateDatabaseEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ShopCartDateDatabaseEntityConverter {

    @TypeConverter
    fun fromEntity(entity: ShopCartDateDatabaseEntity?): String? {
        if (entity == null) return null

        val type: Type = object : TypeToken<ShopCartDateDatabaseEntity?>() {}.type
        return Gson().toJson(entity, type)
    }

    @TypeConverter
    fun toEntity(json: String?): ShopCartDateDatabaseEntity? {
        if (json == null) return null

        val type: Type = object : TypeToken<ShopCartDateDatabaseEntity?>() {}.type
        return Gson().fromJson(json, type)
    }

}