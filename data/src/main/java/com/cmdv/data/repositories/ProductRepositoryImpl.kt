package com.cmdv.data.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cmdv.data.ProductFirebaseEntity
import com.cmdv.data.mappers.ProductFirebaseMapper
import com.cmdv.domain.models.PriceModel
import com.cmdv.domain.models.ProductModel
import com.cmdv.domain.models.QuantityModel
import com.cmdv.domain.repositories.ProductRepository
import com.google.firebase.database.*

const val DB_PRODUCTS_PATH = "products"

class ProductRepositoryImpl : ProductRepository {

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
    ): MutableLiveData<ProductModel> {
        val productMutableLiveData = MutableLiveData<ProductModel>()

        dbProductsRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e(ProductRepositoryImpl::class.java.simpleName, "")
            }

            override fun onDataChange(ds: DataSnapshot) {
                var productId = 0L
                if (ds.exists()) {
                    productId = ds.childrenCount
                }

                dbProductsRef.removeEventListener(this)
                dbProductsRef.child(productId.toString()).setValue(
                    ProductFirebaseMapper().transformModelToEntity(
                        getProductModel(productId, name, costPrice, originalPrice, sellingPrice, quantity, tags)
                    )
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.e(ProductRepositoryImpl::class.java.simpleName, "Product creation task success")
                        // TODO Handle product creation success
                    } else {
                        Log.e(ProductRepositoryImpl::class.java.simpleName, "Product creation task fail")
                        // TODO Handle product creation fail
                    }
                }
            }
        })

        return productMutableLiveData
    }

    private fun getProductModel(
        productId: Long,
        name: String,
        costPrice: String,
        originalPrice: String,
        sellingPrice: String,
        quantity: Int,
        tags: List<String>
    ): ProductModel =
        ProductModel(
            "temp",
            productId,
            name,
            "temp",
            "temp",
            PriceModel(costPrice, originalPrice, sellingPrice),
            QuantityModel(quantity, quantity, 0),
            tags
        )

    /*

    usersDatabaseRef.child(authenticatedUser.uid).setValue(authenticatedUser, DatabaseReference.CompletionListener { p0, p1 ->
			val uidRef = p1.setValue(authenticatedUser)
			uidRef.addOnCompleteListener { task ->
				if (task.isSuccessful) {
					authenticatedUser.let {
						val uid: String = it.uid
						val email: String = it.email
						val displayName: String = it.displayName
						val isNew: Boolean = it.isNew
						val isAuthenticated: Boolean = it.isAuthenticated
						val isCreated = true
						newUserMutableLiveData.postValue(UserModel(uid, email, displayName, isNew, isAuthenticated, isCreated))
					}
				} else {
					Log.d(AuthenticationRepositoryImpl::class.java.simpleName, "${task.exception?.message}")
				}
			}
		})

     */

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