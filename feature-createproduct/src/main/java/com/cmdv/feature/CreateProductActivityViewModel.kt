package com.cmdv.feature

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.ProductModel
import com.cmdv.domain.repositories.ProductRepository

class CreateProductActivityViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    // Product types
    private val _productTypes = MutableLiveData<LiveDataStatusWrapper<ArrayList<String>>>()
    val productTypes: LiveData<LiveDataStatusWrapper<ArrayList<String>>>
        get() = _productTypes

    // Input field value.
    var name: String = ""
        set(value) {
            field = value
            errorEmptyName.postValue(null)
        }
    var productType: String = ""
        set(value) {
            field = value
            errorEmptyProductType.postValue(null)
        }
    var description: String = ""

    var tags: ArrayList<String> = arrayListOf()

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

    var quantity: Int = -1
        set(value) {
            field = value
            errorEmptyQuantity.postValue(null)
        }
    var lowBarrier: Int = -1

    var colorQuantities: ArrayList<Pair<String, Int>> = arrayListOf()

    // Error
    val errorEmptyName = MutableLiveData<Int>()
    val errorEmptyProductType = MutableLiveData<Int>()
    val errorEmptyCostPrice = MutableLiveData<Int>()
    val errorEmptySellingPrice = MutableLiveData<Int>()
    val errorEmptyQuantity = MutableLiveData<Int>()

    fun createProduct(): MutableLiveData<LiveDataStatusWrapper<ProductModel?>>? =
        if (isValidFields()) {
            productRepository.createProduct(
                this.name,
                this.description,
                this.productType,
                this.costPrice,
                this.originalPrice,
                this.sellingPrice,
                this.quantity,
                this.colorQuantities,
                this.lowBarrier,
                this.tags
            )
        } else null

    fun getProductTypes() {
        productRepository.getProductTypes(_productTypes)
    }

    private fun isValidFields(): Boolean {
        var isValidFields = true
        if (name.isEmpty()) {
            errorEmptyName.postValue(R.string.error_input_name_empty)
            isValidFields = false
        }
        if (productType.isEmpty()) {
            errorEmptyProductType.postValue(R.string.error_input_product_type_empty)
            isValidFields = false
        }
        if (costPrice.isEmpty()) {
            errorEmptyCostPrice.postValue(R.string.error_input_cost_price_empty)
            isValidFields = false
        }
        if (sellingPrice.isEmpty()) {
            errorEmptySellingPrice.postValue(R.string.error_input_selling_price_empty)
            isValidFields = false
        }
        if (quantity == 0 || quantity == -1) {
            errorEmptyQuantity.postValue(R.string.error_input_quantity_empty)
            isValidFields = false
        }

        return isValidFields
    }

}