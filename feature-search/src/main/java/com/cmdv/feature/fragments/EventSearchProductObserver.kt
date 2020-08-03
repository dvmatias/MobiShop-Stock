package com.cmdv.feature.fragments

import com.cmdv.domain.models.ProductModel

interface EventSearchProductObserver {

    fun eventShowFiltersAndHistory()

    fun eventOnSearchProductsLoading()

    fun eventOnSearchProductsSuccess(products: List<ProductModel>?)

    fun eventOnSearchProductsError()

}