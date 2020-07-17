package com.cmdv.data.entities.firebase

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
    val tags: List<Map<String, String>>?,
    val date: DateEntity?
) {

    @Suppress("unused")
    constructor() : this(null, null, null, null, null, null, null, null, null, null, null)

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
    val lowBarrier: Int?,
    val colorQuantities: List<ColorQuantityEntity>?
) {

    @Suppress("unused")
    constructor() : this(null, null, null, null, null)

}

data class ColorQuantityEntity(
    val name: String?,
    val value: String?,
    val quantity: Int?
) {

    @Suppress("unused")
    constructor() : this(null, null, null)

}

data class DateEntity(
    val createdDate: String?,
    val updatedDate: String?
) {

    @Suppress("unused")
    constructor() : this(null, null)

}