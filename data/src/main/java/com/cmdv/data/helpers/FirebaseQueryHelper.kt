package com.cmdv.data.helpers

import com.cmdv.data.BuildConfig
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import java.util.*

class FirebaseQueryHelper(private var queryString: String, private val productTypes: ArrayList<String>) {

    private val dbRootRef: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val dbProductsRef: DatabaseReference = dbRootRef.getReference(BuildConfig.DB_PRODUCTS_ROOT_PATH)
    private lateinit var queryWords: ArrayList<String>

    enum class ProductQueryType(val childName: String) {
        BY_CODE("code"),
        BY_NAME("name"),
        BY_TYPE("productType")
    }

    init {
        this.queryString = queryString.toUpperCase(Locale.ROOT)
        setQueryWords()
    }

    fun getProductQuery(): Query {
        val productQueries: ArrayList<Query> = arrayListOf()
        getProductQueryTypes().forEach { productQueryType ->
            productQueries.add(getProductQuery(productQueryType))
        }
        return if (productQueries.isEmpty()) getProductQuery(ProductQueryType.BY_NAME) else productQueries[0]
    }

    fun getProductQueryType(): FirebaseQueryHelper.ProductQueryType =
        getProductQueryTypes()[0]

    private fun setQueryWords() {
        this.queryWords = arrayListOf()
        for (word: String in this.queryString.split(" ")) {
            if (word.isNotEmpty()) {
                this.queryWords.add(word.toLowerCase(Locale.ROOT).trim { it <= ' ' })
            }
        }
    }

    private fun getProductQueryTypes(): List<ProductQueryType> {
        val productQueryTypes: ArrayList<ProductQueryType> = arrayListOf()
        when {
            isFilterByCode() -> productQueryTypes.add(ProductQueryType.BY_CODE)
            isFilterByType() -> productQueryTypes.add(ProductQueryType.BY_TYPE)
            isFilterByName() -> productQueryTypes.add(ProductQueryType.BY_NAME)
        }
        return productQueryTypes
    }

    private fun isFilterByCode(): Boolean =
        this.queryWords.size == 1 && this.queryWords[0].matches("^[1-9]\\d{1,3}\$".toRegex())

    private fun isFilterByType(): Boolean =
        !isFilterByCode() && isQueryStringProductType()

    private fun isQueryStringProductType(): Boolean {
        this.productTypes.forEach {
            if (it.contains(queryString)) {
                return true
            }
        }
        return false
    }

    private fun isFilterByName(): Boolean =
        !isFilterByCode() && this.queryWords.size >= 1

    private fun getProductQuery(productQueryType: ProductQueryType): Query =
         dbProductsRef.orderByChild(productQueryType.childName)
            .startAt(this.queryString)
             .endAt(this.queryString + "\uf8ff")

}