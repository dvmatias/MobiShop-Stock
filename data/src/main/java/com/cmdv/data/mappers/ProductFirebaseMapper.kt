package com.cmdv.data.mappers

import com.cmdv.data.ProductFirebaseEntity
import com.cmdv.core.base.BaseMapper
import com.cmdv.domain.models.ProductModel

class ProductFirebaseMapper: BaseMapper<ProductFirebaseEntity, ProductModel>() {

    override fun transformEntityToModel(e: ProductFirebaseEntity): ProductModel {
        return ProductModel()
    }
}