package com.cmdv.feature.ui.fragments.salestabs

import androidx.lifecycle.ViewModel
import com.cmdv.domain.repositories.ShopCartRepository

class ShopCartSectionTabFragmentViewModel(
    shopCartRepository: ShopCartRepository
) : ViewModel() {

    val test = shopCartRepository.getAllOpenShopCarts()

}