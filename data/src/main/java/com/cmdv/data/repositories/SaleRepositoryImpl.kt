package com.cmdv.data.repositories

import androidx.lifecycle.MutableLiveData
import com.cmdv.data.BuildConfig
import com.cmdv.data.entities.firebase.SaleFirebaseEntity
import com.cmdv.data.mappers.SaleFirebaseMapper
import com.cmdv.domain.datasources.firebase.SaleStoreListener
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.SaleModel
import com.cmdv.domain.models.ShopCartModel
import com.cmdv.domain.repositories.SaleRepository
import com.google.firebase.database.*

class SaleRepositoryImpl : SaleRepository {

    private val dbRootRef: FirebaseDatabase = FirebaseDatabase.getInstance()

    private val dbSalesRef: DatabaseReference = dbRootRef.getReference(BuildConfig.DB_SALES_ROOT_PATH)

    override fun createSale(shopCart: ShopCartModel, sale: SaleModel, listener: SaleStoreListener) {
        val saleFirebase: SaleFirebaseEntity =
            SaleFirebaseMapper().transformModelToEntity(sale)
        dbSalesRef.child(saleFirebase.id.toString()).setValue(
            saleFirebase
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                listener.onSuccess(shopCart)
            } else {
                listener.onFailure()
            }
        }
    }

    override fun getSales(liveData: MutableLiveData<LiveDataStatusWrapper<List<SaleModel>>>) {
        dbSalesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val sales = ArrayList<SaleModel>()
                snapshot.children.forEach {dataSnapshot ->
                    val saleFirebase: SaleFirebaseEntity? = dataSnapshot.getValue(SaleFirebaseEntity::class.java)
                    saleFirebase?.let {
                        sales.add(SaleFirebaseMapper().transformEntityToModel(it))
                    }
                }

                liveData.value = LiveDataStatusWrapper.success(sales)
            }

            override fun onCancelled(error: DatabaseError) {
                liveData.value = LiveDataStatusWrapper.error("", null)
            }
        })
    }
}