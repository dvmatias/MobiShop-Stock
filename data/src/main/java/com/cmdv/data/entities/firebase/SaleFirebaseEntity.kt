package com.cmdv.data.entities.firebase

data class SaleFirebaseEntity(
    val id: Long?,
    val name: String?,
    val date: DateFirebaseEntity?,
    val productCodes: List<String>?,
    val productQuantity: Int?,
    val discount: Float?,
    val total: Float?
) {

    constructor() : this(null, null, null, null, null, null, null)

    data class DateFirebaseEntity(
        val createdDate: String?,
        val closedDate: String?
    ) {

        constructor() : this(null, null)

    }

}