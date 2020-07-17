package com.cmdv.feature

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmdv.core.Constants.Companion.DATE_FORMAT_DD_MM_YY_HH_MM_SS
import com.cmdv.domain.models.*
import com.cmdv.domain.repositories.ProductRepository
import java.text.SimpleDateFormat
import java.util.*

class EditProductActivityViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    // Input field value.
    var name: String = ""
        set(value) {
            field = value
            errorEmptyName.postValue(null)
        }

    var description: String = ""

    var costPrice: String = ""
        set(value) {
            field = value
            errorEmptyCostPrice.postValue(null)
        }

    var sellingPrice: String = ""
        set(value) {
            field = value
            errorEmptySellingPrice.postValue(null)
        }

    var originalPrice: String = ""

    var initialQuantity: Int = -1
        set(value) {
            field = value
            errorEmptyInitialQuantity.postValue(null)
        }

    var availableQuantity: Int = -1
        set(value) {
            field = value
            errorEmptyAvailableQuantity.postValue(null)
        }

    var soldQuantity: Int = -1
        set(value) {
            field = value
            errorEmptySoldQuantity.postValue(null)
        }

    var colorQuantities: ArrayList<ColorQuantityModel> = arrayListOf()

    // Error
    val errorEmptyName = MutableLiveData<Int>()
    val errorEmptyCostPrice = MutableLiveData<Int>()
    val errorEmptySellingPrice = MutableLiveData<Int>()
    val errorEmptyInitialQuantity = MutableLiveData<Int>()
    val errorEmptyAvailableQuantity = MutableLiveData<Int>()
    val errorEmptySoldQuantity = MutableLiveData<Int>()

    private val _productMutableLiveData = MutableLiveData<LiveDataStatusWrapper<ProductModel>>()
    val productLiveData: LiveData<LiveDataStatusWrapper<ProductModel>>
        get() = _productMutableLiveData

    fun updateProduct(product: ProductModel) {
        if (checkFieldsValidity()) {
            val updatedProduct = getUpdatedProductModel(product)
            if (isSameProduct(product)) {
                _productMutableLiveData.value = LiveDataStatusWrapper.error("No diff", null)
            } else {
                productRepository.updateProduct(
                    _productMutableLiveData,
                    product.id.toInt(),
                    updatedProduct
                )
            }
        }
    }

    private fun isSameProduct(product: ProductModel): Boolean =
        (this.name == product.name && this.description == product.description && this.costPrice == product.price.costPrice &&
                this.sellingPrice == product.price.sellingPrice && this.originalPrice == product.price.originalPrice &&
                this.initialQuantity == product.quantity.initial && this.availableQuantity == product.quantity.available &&
                this.soldQuantity == product.quantity.sold)

    private fun getUpdatedProductModel(product: ProductModel): ProductModel =
        ProductModel(
            product.code,
            product.id,
            product.productType,
            this.name,
            this.description,
            product.model,
            product.imageName,
            PriceModel(
                this.costPrice,
                this.originalPrice,
                this.sellingPrice
            ),
            QuantityModel(
                this.initialQuantity,
                this.availableQuantity,
                this.soldQuantity,
                product.quantity.lowBarrier,
                this.colorQuantities
            ),
            product.tags,
            DateModel(
                product.date.createdDate,
                SimpleDateFormat(DATE_FORMAT_DD_MM_YY_HH_MM_SS, Locale.getDefault()).format(Date().time)
            )
        )

    private fun checkFieldsValidity(): Boolean {
        var validFields = true
        if (name.isEmpty()) {
            validFields = false
            errorEmptyName.postValue(R.string.error_input_name_empty)
        }
        if (costPrice.isEmpty()) {
            validFields = false
            errorEmptyCostPrice.postValue(R.string.error_input_cost_price_empty)
        }
        if (sellingPrice.isEmpty()) {
            errorEmptySellingPrice.postValue(R.string.error_input_selling_price_empty)
            validFields = false
        }
        if (initialQuantity == -1) {
            errorEmptyInitialQuantity.postValue(R.string.error_input_initial_quantity_empty)
            validFields = false
        }
        if (availableQuantity == -1) {
            errorEmptyAvailableQuantity.postValue(R.string.error_input_available_quantity_empty)
            validFields = false
        }
        if (soldQuantity == -1) {
            errorEmptySoldQuantity.postValue(R.string.error_input_sold_quantity_empty)
            validFields = false
        }
        return validFields
    }
}