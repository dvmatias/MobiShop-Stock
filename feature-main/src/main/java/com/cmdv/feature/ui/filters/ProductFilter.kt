package com.cmdv.feature.ui.filters

import android.widget.Filter
import com.cmdv.core.utils.logErrorMessage
import com.cmdv.domain.models.ProductModel
import com.cmdv.feature.ui.adapters.ProductRecyclerAdapter
import java.util.*

internal class ProductFilter(
    private val productRecyclerAdapter: ProductRecyclerAdapter,
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
                for (product: ProductModel in fullData) {
                    queryWords.forEach { queryWord ->
                        when(queryWords.size) {
                            1 -> {
                                searchForWord(queryWord, product)
                            }
                            else -> {
                                searchForSentence(constraint.toString(), product)
                            }
                        }
                    }
                }
                // Filtered by sentences but no result found.
                if (queryWords.size > 1 && filteredData.size == 0) {
                    logErrorMessage("Filtered by sentences but no result found.")
                    for (product in fullData) {
                        queryWords.subList(0, 2).forEach {queryWord: String ->
                            searchForWord(queryWord, product)
                        }
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
        productRecyclerAdapter.setProducts(filteredData)
    }

    /**
     * Check if the query search has the form of a product code
     */
    private fun shouldFilterByCode() =
        queryWords.size == 1 && queryWords[0].matches("^[1-9]\\d{1,3}\$".toRegex())

    /**
     * Search a product by a single query word.
     */
    private fun searchForWord(queryWord: String, product: ProductModel) {
        logErrorMessage("Search for Word  >>  $queryWord")
        if (!isValidWordToSearch(queryWord)) {
            return
        }
        addIfContainsWord(product, product.productType.split(" "), queryWord)
        addIfContainsWord(product, product.name.split(" "), queryWord)
        addIfContainsWord(product, product.tags, queryWord)
    }

    private fun isValidWordToSearch(queryWord: String): Boolean =
        queryWord.length >= 2

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

    /**
     * Search a product for a sentence (two or more query words).
     */
    private fun searchForSentence(querySentence: String, product: ProductModel) {
        logErrorMessage("Search for Sentence  >>  $querySentence")
        addIfContainsSentence(product, product.productType, querySentence)
        addIfContainsSentence(product, product.name, querySentence)
    }

    private fun addIfContainsSentence(
        product: ProductModel,
        keySentence: String,
        querySentence: String
    ) {
        if (!filteredData.contains(product)) {
            if (keySentence.contains(querySentence, true)) {
                filteredData.add(product)
                return
            }
        }
    }

}