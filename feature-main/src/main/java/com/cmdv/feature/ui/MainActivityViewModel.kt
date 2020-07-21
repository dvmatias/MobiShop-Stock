package com.cmdv.feature.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmdv.core.Constants
import com.cmdv.domain.models.ShopCartModel
import com.cmdv.domain.repositories.ShopCartRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivityViewModel(
    private val shopCartRepository: ShopCartRepository
) : ViewModel() {

    // Shop Cart List
    val liveDataOpenShopCarts = shopCartRepository.getAllOpenShopCarts()

    fun createShopCart(name: String) = viewModelScope.launch {
        shopCartRepository.insertShopCart(
            name,
            SimpleDateFormat(Constants.DATE_FORMAT_DD_MM_YY_HH_MM_SS, Locale.getDefault()).format(Date().time)
        )
    }

    suspend fun addShopCartProduct(shopCartId: Long, product: ShopCartModel.ShopCartProductModel) {
        shopCartRepository.addProduct(shopCartId, product)
    }

}