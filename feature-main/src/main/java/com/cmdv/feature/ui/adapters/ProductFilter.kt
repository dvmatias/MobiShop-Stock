package com.cmdv.feature.ui.adapters

import android.widget.Filter
import com.cmdv.core.utils.logErrorMessage
import com.cmdv.domain.models.ProductModel
import java.util.*

internal class ProductFilter(
    private val adapter: RecyclerProductAdapter,
    private val fullData: ArrayList<ProductModel>
) : Filter() {

    private var filteredData: ArrayList<ProductModel> = arrayListOf()

    private val queryWords = arrayListOf<String>()

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        filteredData.clear()
        val results = FilterResults()

        if (constraint.isNullOrEmpty() || constraint.length <= 1) {
            filteredData.addAll(fullData)
        } else {
            for (queryWord: String in constraint.toString().split(" ")) {
                if (queryWord.isNotEmpty()) {
                    queryWords.add(queryWord.toLowerCase(Locale.ROOT).trim { it <= ' ' })
                }
            }

            if (shouldFilterByCode()) {
                logErrorMessage("Should filter by code")
                for (product in fullData) {
                    if (!filteredData.contains(product) && product.code.contains(queryWords[0])) {
                        filteredData.add(product)
                    }
                }
            } else {
                logErrorMessage("Should filter by Type, Name and Tags")
                for (product in fullData) {
                    for (queryWord in queryWords) {
                        addIfContainsWord(product, product.productType.split(" "), queryWord)
                        addIfContainsWord(product, product.name.split(" "), queryWord)
                        addIfContainsWord(product, product.tags, queryWord)
                        // Once the product has been added, no need to keep searching keywords here
                        if (filteredData.contains(product))
                            break
                    }
                }

            }
        }

        results.values = filteredData
        results.count = filteredData.size
        return results
    }

    override fun convertResultToString(resultValue: Any?): CharSequence {
        return super.convertResultToString(resultValue)
    }

    override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
        adapter.setProducts(filteredData)
    }

    /**
     * Check if the query search has the form of a product code
     */
    private fun shouldFilterByCode() =
        queryWords.size == 1 && queryWords[0].matches("^[1-9]\\d{1,3}\$".toRegex())

    /**
     * Check if the array of key words contain the query word, if does it then the product
     * is added to the filtered results.
     */
    private fun addIfContainsWord(
        product: ProductModel,
        keyWords: List<String>,
        queryWord: String
    ) {
        if (!filteredData.contains(product)) {
            for (keyWord in keyWords) {
                if (keyWord.contains(queryWord, true)) {
                    filteredData.add(product)
                    break
                }
            }
        }
    }

}