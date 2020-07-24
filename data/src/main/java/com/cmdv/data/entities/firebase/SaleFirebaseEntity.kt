package com.cmdv.data.entities.firebase

data class SaleFirebaseEntity (
    val id: Long?,
    val name: String?,
    val date: DateFirebaseEntity?,
    val productCodes: List<String>?,
    val productQuantity: Int?,
    val discount: Float?,
    val total: Float?
) {

    data class DateFirebaseEntity(
        val createdDate: String?,
        val closedDate: String?
    )

}