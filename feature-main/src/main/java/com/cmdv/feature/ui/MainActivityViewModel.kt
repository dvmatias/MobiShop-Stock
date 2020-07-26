package com.cmdv.feature.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmdv.core.Constants
import com.cmdv.domain.datasources.firebase.SaleStoreListener
import com.cmdv.domain.datasources.firebase.UserStoreListener
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.SaleModel
import com.cmdv.domain.models.ShopCartModel
import com.cmdv.domain.models.UserModel
import com.cmdv.domain.repositories.ProductRepository
import com.cmdv.domain.repositories.SaleRepository
import com.cmdv.domain.repositories.ShopCartRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivityViewModel(
    private val shopCartRepository: ShopCartRepository,
    private val saleRepository: SaleRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    // Shop Cart List
    val liveDataOpenShopCarts = shopCartRepository.getAllOpenShopCarts()

    suspend fun getShopCartCount(): Int {
        return shopCartRepository.getDataCount()
    }

    suspend fun getOpenShopCart(): ShopCartModel? {
        return shopCartRepository.getOpenShopCart()
    }

    fun createShopCart(name: String) = viewModelScope.launch {
        shopCartRepository.insertShopCart(
            name,
            SimpleDateFormat(Constants.DATE_FORMAT_DD_MM_YY_HH_MM_SS, Locale.getDefault()).format(Date().time)
        )
    }

    suspend fun addShopCartProduct(shopCartId: Long, product: ShopCartModel.ShopCartProductModel) {
        shopCartRepository.addProduct(shopCartId, product)
    }

    // Sale
    private var _mutableLiveDataSale = MutableLiveData<LiveDataStatusWrapper<SaleModel>>()
    val liveDataSale: LiveData<LiveDataStatusWrapper<SaleModel>>
        get() = _mutableLiveDataSale

    fun closeShoppingCart(shopCart: ShopCartModel) {
        saleRepository.createSale(shopCart, createSaleFromShopCart(shopCart), saleStoreListener)
    }

    private fun createSaleFromShopCart(shopCart: ShopCartModel): SaleModel =
        SaleModel(
            shopCart.id,
            shopCart.name,
            SaleModel.DateModel(
                shopCart.date.createdDate,
                SimpleDateFormat(Constants.DATE_FORMAT_DD_MM_YY_HH_MM_SS, Locale.getDefault()).format(Date().time)),
            shopCart.products.map { it.code },
            getShopCartProductsQuantity(shopCart.products),
            shopCart.discount.toFloat(),
            getShopCartTotalPrice(shopCart.products)
        )

    private fun getShopCartProductsQuantity(products: ArrayList<ShopCartModel.ShopCartProductModel>): Int {
        var productsQuantity = 0
        products.forEach {
            it.colorQuantity.forEach { colorQuantity ->
                productsQuantity += colorQuantity.colorQuantity
            }
        }
        return productsQuantity
    }

    private fun getShopCartTotalPrice(products: ArrayList<ShopCartModel.ShopCartProductModel>): Float {
        var totalPrice = 0F
        products.forEach { product ->
            product.colorQuantity.forEach { colorQuantity ->
                totalPrice += product.price.toFloat() * colorQuantity.colorQuantity
            }
        }
        return totalPrice
    }

    suspend fun deleteShopCart(shopCart: ShopCartModel) {
        shopCartRepository.deleteShopCart(shopCart)
    }

    suspend fun deleteProductsInDatabase(shopCart: ShopCartModel) {
        productRepository.saleProductsInShopCart(shopCart)
    }

    private var saleStoreListener = object : SaleStoreListener {
        override fun onSuccess(shopCart: ShopCartModel) {
            GlobalScope.launch {
                deleteShopCart(shopCart)
                deleteProductsInDatabase(shopCart)
            }
        }

        override fun onFailure() {

        }
    }
}