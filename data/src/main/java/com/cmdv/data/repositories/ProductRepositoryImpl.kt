package com.cmdv.data.repositories

import androidx.lifecycle.MutableLiveData
import com.cmdv.data.BuildConfig
import com.cmdv.data.entities.ProductFirebaseEntity
import com.cmdv.data.mappers.ProductFirebaseMapper
import com.cmdv.domain.models.*
import com.cmdv.domain.repositories.ProductRepository
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val DATE_FORMAT_DD_MM_YY = "dd-MM-yyyy'T'HH:mm:ss.SSS"

class ProductRepositoryImpl : ProductRepository {

    var productMutableLiveData = MutableLiveData<LiveDataStatusWrapper<ProductModel?>>()

    private val dbRootRef: FirebaseDatabase = FirebaseDatabase.getInstance()

    private val dbProductsRef: DatabaseReference = dbRootRef.getReference(BuildConfig.DB_PRODUCTS_ROOT_PATH)

    private val dbProductTypeRef: DatabaseReference = dbRootRef.getReference(BuildConfig.DB_PRODUCT_TYPE_ROOT_PATH)

    override fun updateProduct(
        productMutableLiveData: MutableLiveData<LiveDataStatusWrapper<ProductModel>>,
        id: Int,
        product: ProductModel
    ) {
        // Set loading status.
        productMutableLiveData.value = LiveDataStatusWrapper.loading(null)

        val productFirebase: ProductFirebaseEntity =
            ProductFirebaseMapper().transformModelToEntity(product)
        dbProductsRef.child(id.toString()).setValue(
            productFirebase
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                productMutableLiveData.value =
                    LiveDataStatusWrapper.success(
                        ProductFirebaseMapper().transformEntityToModel(productFirebase)
                    )
            } else {
                productMutableLiveData.value =
                    LiveDataStatusWrapper.error("There was an error trying to update product with id-$id.", null)
            }
        }
    }

    override fun createProduct(
        name: String,
        description: String,
        productType: String,
        costPrice: String,
        originalPrice: String,
        sellingPrice: String,
        quantity: Int,
        colorQuantities: ArrayList<Pair<String, Int>>,
        lowBarrier: Int,
        tags: List<String>
    ): MutableLiveData<LiveDataStatusWrapper<ProductModel?>> {
        // Reset Live data object to avoid older values.
        productMutableLiveData = MutableLiveData()
        // Set loading status.
        productMutableLiveData.value = (LiveDataStatusWrapper.loading(null))

        dbProductsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val id: Long = dataSnapshot.childrenCount
                val code: String = generateUniqueRandomProductCode(dataSnapshot)
                val productFirebase: ProductFirebaseEntity =
                    ProductFirebaseMapper().transformModelToEntity(
                        getProductModel(
                            code,
                            id,
                            productType,
                            name,
                            description,
                            costPrice,
                            originalPrice,
                            sellingPrice,
                            quantity,
                            colorQuantities,
                            lowBarrier,
                            tags
                        )
                    )

                dbProductsRef.child(id.toString()).setValue(productFirebase)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            productMutableLiveData.value =
                                LiveDataStatusWrapper.success(
                                    ProductFirebaseMapper().transformEntityToModel(productFirebase)
                                )
                        } else {
                            productMutableLiveData.value =
                                LiveDataStatusWrapper.error("", null)
                        }
                    }
            }
        })

        return productMutableLiveData
    }

    /**
     * Generates a random code of four digits and return as String.
     * This value must be unique in DB so only one product can have it.
     */
    private fun generateUniqueRandomProductCode(dataSnapshot: DataSnapshot): String {
        val randomCode = (1000..9999).random().toString()
        for (ds in dataSnapshot.children) {
            val productFirebase: ProductFirebaseEntity? =
                ds.getValue(ProductFirebaseEntity::class.java)
            if (productFirebase != null && productFirebase.code.equals(randomCode)) {
                generateUniqueRandomProductCode(dataSnapshot)
            }
        }
        return randomCode
    }

    private fun getProductModel(
        code: String,
        id: Long,
        productType: String,
        name: String,
        description: String,
        costPrice: String,
        originalPrice: String,
        sellingPrice: String,
        quantity: Int,
        colorQuantities: ArrayList<Pair<String, Int>>,
        lowBarrier: Int,
        tags: List<String>
    ): ProductModel {

        val dateString = SimpleDateFormat(DATE_FORMAT_DD_MM_YY, Locale.getDefault()).format(Date().time)

        return ProductModel(
            code,
            id,
            productType,
            name,
            description,
            "temp",
            "temp",
            PriceModel(costPrice, if (originalPrice.isEmpty()) sellingPrice else originalPrice, sellingPrice),
            QuantityModel(quantity, quantity, 0, lowBarrier, colorQuantities),
            tags,
            DateModel(dateString, dateString)
        )
    }

    override fun getProducts(productsMutableLiveData: MutableLiveData<LiveDataStatusWrapper<List<ProductModel>>>) {
        productsMutableLiveData.value = LiveDataStatusWrapper.loading(null)
        dbProductsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = ArrayList<ProductModel>()
                for (ds in snapshot.children) {
                    val productFirebaseEntity: ProductFirebaseEntity? =
                        ds.getValue(ProductFirebaseEntity::class.java)
                    if (productFirebaseEntity != null) {
                        products.add(
                            ProductFirebaseMapper().transformEntityToModel(
                                productFirebaseEntity
                            )
                        )
                    }
                }
                productsMutableLiveData.value = LiveDataStatusWrapper.success(products)
            }

            override fun onCancelled(error: DatabaseError) {
                productsMutableLiveData.value = LiveDataStatusWrapper.error("", null)
            }
        })
    }

    override fun getProductTypes(mutableLiveData: MutableLiveData<LiveDataStatusWrapper<ArrayList<String>>>) {
        // Set loading status.
        mutableLiveData.value = (LiveDataStatusWrapper.loading(null))
        // Get product types
        dbProductTypeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val productTypes: ArrayList<String> = arrayListOf()
                    for (child: DataSnapshot in snapshot.children) {
                        child.getValue(String::class.java)?.let {
                            productTypes.add(it)
                        }
                    }
                    // Sort alphabetically and add dumb first item
                    Collections.sort(productTypes, String.CASE_INSENSITIVE_ORDER)
                    mutableLiveData.value = (LiveDataStatusWrapper.success(productTypes))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                mutableLiveData.value =
                    (LiveDataStatusWrapper.error(error.message, null))
            }
        })
    }
}