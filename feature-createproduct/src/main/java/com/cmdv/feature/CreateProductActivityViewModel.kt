package com.cmdv.feature

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmdv.domain.models.CreateProductStatusWrapper
import com.cmdv.domain.models.ProductModel
import com.cmdv.domain.repositories.ProductRepository

class CreateProductActivityViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    // Input field value.
    var name: String = ""
        set(value) {
            field = value
            errorEmptyName.postValue(null)
        }
    var costPrice: String = ""
        set(value) {
            field = value
            errorEmptyCostPrice.postValue(null)
        }
    var originalPrice: String = ""
        set(value) {
            field = value
            errorEmptyOriginalPrice.postValue(null)
        }
    var sellingPrice: String = ""
        set(value) {
            field = value
            errorEmptySellingPrice.postValue(null)
        }
    var quantity: Int = -1
        set(value) {
            field = value
            errorEmptyQuantity.postValue(null)
        }
    var tags: ArrayList<String> = arrayListOf()

    // Error
    val errorEmptyName = MutableLiveData<Int>()
    val errorEmptyCostPrice = MutableLiveData<Int>()
    val errorEmptyOriginalPrice = MutableLiveData<Int>()
    val errorEmptySellingPrice = MutableLiveData<Int>()
    val errorEmptyQuantity = MutableLiveData<Int>()

    fun createProduct(): MutableLiveData<CreateProductStatusWrapper<ProductModel?>>? =
        if (isValidFields()) {
            productRepository.createProduct(
                this.name,
                this.costPrice,
                this.originalPrice,
                this.sellingPrice,
                this.quantity,
                this.tags
            )
        } else null

    private fun isValidFields(): Boolean {
        var isValidFields = true
        if (name.isEmpty()) {
            errorEmptyName.postValue(R.string.error_input_name_empty)
            isValidFields = false
        }
        if (costPrice.isEmpty()) {
            errorEmptyCostPrice.postValue(R.string.error_input_cost_price_empty)
            isValidFields = false
        }
        if (originalPrice.isEmpty()) {
            errorEmptyOriginalPrice.postValue(R.string.error_input_original_price_empty)
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