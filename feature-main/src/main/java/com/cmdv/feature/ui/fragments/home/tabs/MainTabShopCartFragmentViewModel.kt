package com.cmdv.feature.ui.fragments.home.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmdv.domain.repositories.ShopCartRepository
import kotlinx.coroutines.launch

class MainTabShopCartFragmentViewModel(
    private val shopCartRepository: ShopCartRepository
) : ViewModel() {

    val liveDataOpenShopCarts = shopCartRepository.getAllOpenShopCarts()

    fun deleteAll() = viewModelScope.launch {
        shopCartRepository.deleteAll()
    }

}