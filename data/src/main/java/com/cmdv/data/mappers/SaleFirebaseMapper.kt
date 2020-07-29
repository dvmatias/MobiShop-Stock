package com.cmdv.data.mappers

import com.cmdv.data.entities.firebase.SaleFirebaseEntity
import com.cmdv.domain.mapper.BaseMapper
import com.cmdv.domain.models.SaleModel

class SaleFirebaseMapper : BaseMapper<SaleFirebaseEntity, SaleModel>() {

    override fun transformModelToEntity(m: SaleModel): SaleFirebaseEntity {
        val id: Long? = m.id
        val name: String? = m.name
        val date: SaleFirebaseEntity.DateFirebaseEntity? = SaleFirebaseEntity.DateFirebaseEntity(m.date.createdDate, m.date.closedDate)
        val productCodes: List<String>? = m.productCodes
        val productQuantity: Int? = m.productQuantity
        val discount: Float? = m.discount
        val total: Float? = m.total

        return SaleFirebaseEntity(id, name, date, productCodes, productQuantity, discount, total)
    }

    override fun transformEntityToModel(e: SaleFirebaseEntity): SaleModel {
        val id: Long = e.id ?: -1
        val name: String = e.name ?: ""
        val date: SaleModel.DateModel = SaleModel.DateModel(e.date?.createdDate ?: "", e.date?.closedDate ?: "")
        val productCodes: List<String> = e.productCodes ?: listOf()
        val productQuantity: Int = e.productQuantity ?: 0
        val discount: Float = e.discount ?: 0F
        val total: Float = e.total ?: 0F

        return SaleModel(id, name, date, productCodes, productQuantity, discount, total)
    }

}