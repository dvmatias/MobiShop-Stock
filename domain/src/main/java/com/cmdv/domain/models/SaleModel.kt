package com.cmdv.domain.models

data class SaleModel (
    val id: Long,
    val name: String,
    val date: DateModel,
    val productCodes: List<String>,
    val productQuantity: Int,
    val discount: Float,
    val total: Float
) {

    data class DateModel(
        val createdDate: String,
        val closedDate: String
    )

}