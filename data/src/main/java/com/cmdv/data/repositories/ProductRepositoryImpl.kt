package com.cmdv.data.repositories

import androidx.lifecycle.MutableLiveData
import com.cmdv.core.utils.LogUtil
import com.cmdv.data.ProductFirebaseEntity
import com.cmdv.data.mappers.ProductFirebaseMapper
import com.cmdv.domain.models.ProductModel
import com.cmdv.domain.repositories.ProductRepository
import com.google.firebase.database.*

const val DB_PRODUCTS_PATH = "products"

class ProductRepositoryImpl : ProductRepository {

    private val dbRootRef: FirebaseDatabase = FirebaseDatabase.getInstance()

    private val dbProductsRef: DatabaseReference = dbRootRef.getReference(DB_PRODUCTS_PATH)

    override fun getProducts(): MutableLiveData<List<ProductModel>> {
        val productsMutableLiveData = MutableLiveData<List<ProductModel>>()

        dbProductsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = ArrayList<ProductModel>()
                for (ds in snapshot.children) {
                    val productFirebaseEntity: ProductFirebaseEntity? = ds.getValue(ProductFirebaseEntity::class.java)
                    if (productFirebaseEntity!= null) {
                        products.add(ProductFirebaseMapper().transformEntityToModel(productFirebaseEntity))
                    }
                }
                productsMutableLiveData.postValue(products)
            }

            override fun onCancelled(error: DatabaseError) {
                LogUtil.logErrorMessage(error.message)
            }
        })

        return productsMutableLiveData
    }

}