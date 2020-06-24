package com.cmdv.data.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cmdv.data.ProductFirebaseEntity
import com.cmdv.data.mappers.ProductFirebaseMapper
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.PriceModel
import com.cmdv.domain.models.ProductModel
import com.cmdv.domain.models.QuantityModel
import com.cmdv.domain.repositories.ProductRepository
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

const val DB_PRODUCTS_PATH = "products"
const val DB_PRODUCT_TYPE_PATH = "productType"

class ProductRepositoryImpl : ProductRepository {

    var productMutableLiveData = MutableLiveData<LiveDataStatusWrapper<ProductModel?>>()

    private val dbRootRef: FirebaseDatabase = FirebaseDatabase.getInstance()

    private val dbProductsRef: DatabaseReference = dbRootRef.getReference(DB_PRODUCTS_PATH)

    private val dbProductTypeRef: DatabaseReference = dbRootRef.getReference(DB_PRODUCT_TYPE_PATH)

    override fun updateProduct(id: Int, product: ProductModel): MutableLiveData<ProductModel> {
        TODO("Not yet implemented")
    }

    override fun createProduct(
        name: String,
        description: String,
        productType: String,
        costPrice: String,
        originalPrice: String,
        sellingPrice: String,
        quantity: Int,
        tags: List<String>
    ): MutableLiveData<LiveDataStatusWrapper<ProductModel?>> {
        // Reset Live data object to avoid older values.
        productMutableLiveData = MutableLiveData()
        // Set loading status.
        productMutableLiveData.value = (LiveDataStatusWrapper.loading(null))

        dbProductsRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var id = 0L
                var code = ""
                if (dataSnapshot.exists()) {
                    id = dataSnapshot.childrenCount
                    code = generateUniqueRandomCode(dataSnapshot)
                }

                dbProductsRef.removeEventListener(this)

                val productFirebase: ProductFirebaseEntity =
                    ProductFirebaseMapper().transformModelToEntity(
                        getProductModel(code, id, productType, name, description, costPrice, originalPrice, sellingPrice, quantity, tags)
                    )
                dbProductsRef.child(id.toString()).setValue(productFirebase)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            productMutableLiveData.value =
                                LiveDataStatusWrapper.success(
                                    ProductFirebaseMapper().transformEntityToModel(productFirebase))
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
    private fun generateUniqueRandomCode(dataSnapshot: DataSnapshot): String {
        val randomCode = (1000..9999).random().toString()
        for (ds in dataSnapshot.children) {
            val productFirebase: ProductFirebaseEntity? =
                ds.getValue(ProductFirebaseEntity::class.java)
            if (productFirebase != null && productFirebase.code.equals(randomCode)) {
                generateUniqueRandomCode(dataSnapshot)
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
        tags: List<String>
    ): ProductModel =
        ProductModel(
            code,
            id,
            productType,
            name,
            description,
            "temp",
            "temp",
            PriceModel(costPrice, originalPrice, sellingPrice),
            QuantityModel(quantity, quantity, 0),
            tags
        )

    override fun getProducts(productsMutableLiveData: MutableLiveData<LiveDataStatusWrapper<List<ProductModel>>>) {
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