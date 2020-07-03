package com.cmdv.data

data class ProductFirebaseEntity(
    val code: String?,
    val id: Long?,
    val productType: String?,
    val name: String?,
    val description: String?,
    val model: String?,
    val imageName: String?,
    val price: PriceEntity?,
    val quantity: QuantityEntity?,
    val tags: List<Map<String, String>>?
) {

    @Suppress("unused")
    constructor() : this(null, null, null, null, null, null, null, null, null, null)

}

data class PriceEntity(
    val costPrice: String?,
    val originalPrice: String?,
    val sellingPrice: String?
) {

    @Suppress("unused")
    constructor() : this(null, null, null)

}

data class QuantityEntity(
    val initial: Int?,
    val available: Int?,
    val sold: Int?,
    val lowBarrier: Int?
) {

    @Suppress("unused")
    constructor() : this(null, null, null, null)

}