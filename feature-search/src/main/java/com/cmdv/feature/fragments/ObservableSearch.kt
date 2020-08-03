package com.cmdv.feature.fragments

import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.ProductModel

interface ObservableSearch {

    fun registerObserver(eventSearchProductObserver: EventSearchProductObserver)

    fun removeObserver(eventSearchProductObserver: EventSearchProductObserver)

    fun notifyObservers(dataStatusWrapper: LiveDataStatusWrapper<List<ProductModel>>?)

}