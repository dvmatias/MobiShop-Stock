package com.cmdv.data.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cmdv.data.ProductFirebaseEntity
import com.cmdv.data.mappers.ProductFirebaseMapper
import com.cmdv.domain.models.PriceModel
import com.cmdv.domain.models.ProductCreationStatusModel
import com.cmdv.domain.models.ProductModel
import com.cmdv.domain.models.QuantityModel
import com.cmdv.domain.repositories.ProductRepository
import com.google.firebase.database.*

const val DB_PRODUCTS_PATH = "products"

class ProductRepositoryImpl : ProductRepository {

    var productMutableLiveData = MutableLiveData<ProductCreationStatusModel<ProductModel?>>()

    private val dbRootRef: FirebaseDatabase = FirebaseDatabase.getInstance()

    private val dbProductsRef: DatabaseReference = dbRootRef.getReference(DB_PRODUCTS_PATH)

    override fun updateProduct(id: Int, product: ProductModel): MutableLiveData<ProductModel> {
        TODO("Not yet implemented")
    }

    override fun createProduct(
        name: String,
        costPrice: String,
        originalPrice: String,
        sellingPrice: String,
        quantity: Int,
        tags: List<String>
    ): MutableLiveData<ProductCreationStatusModel<ProductModel?>> {
        productMutableLiveData.value = (ProductCreationStatusModel.loading(null))
        dbProductsRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e(ProductRepositoryImpl::class.java.simpleName, "")
            }

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
                        getProductModel(
                            code,
                            id,
                            name,
                            costPrice,
                            originalPrice,
                            sellingPrice,
                            quantity,
                            tags
                        )
                    )
                dbProductsRef.child(id.toString()).setValue(productFirebase).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.e(
                            ProductRepositoryImpl::class.java.simpleName,
                            "Product creation task success"
                        )
                        productMutableLiveData.value = ProductCreationStatusModel.success(
                            ProductFirebaseMapper().transformEntityToModel(productFirebase)
                        )
                    } else {
                        Log.e(
                            ProductRepositoryImpl::class.java.simpleName,
                            "Product creation task fail"
                        )
                        productMutableLiveData.value = ProductCreationStatusModel.error("", null)
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
        name: String,
        costPrice: String,
        originalPrice: String,
        sellingPrice: String,
        quantity: Int,
        tags: List<String>
    ): ProductModel =
        ProductModel(
            code,
            id,
            name,
            "temp",
            "temp",
            PriceModel(costPrice, originalPrice, sellingPrice),
            QuantityModel(quantity, quantity, 0),
            tags
        )

    override fun getProducts(): MutableLiveData<List<ProductModel>> {
        val productsMutableLiveData = MutableLiveData<List<ProductModel>>()

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
                productsMutableLiveData.postValue(products)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        return productsMutableLiveData
    }
}