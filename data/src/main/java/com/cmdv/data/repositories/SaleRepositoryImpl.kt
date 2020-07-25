package com.cmdv.data.repositories

import androidx.lifecycle.MutableLiveData
import com.cmdv.data.BuildConfig
import com.cmdv.data.entities.firebase.SaleFirebaseEntity
import com.cmdv.data.mappers.SaleFirebaseMapper
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.SaleModel
import com.cmdv.domain.repositories.SaleRepository
import com.google.firebase.database.*

class SaleRepositoryImpl : SaleRepository {

    private val dbRootRef: FirebaseDatabase = FirebaseDatabase.getInstance()

    private val dbSalesRef: DatabaseReference = dbRootRef.getReference(BuildConfig.DB_SALES_ROOT_PATH)

    override fun createSale(liveData:  MutableLiveData<LiveDataStatusWrapper<SaleModel>>, sale: SaleModel) {
        liveData.value = LiveDataStatusWrapper.loading(null)

        val saleFirebase: SaleFirebaseEntity =
            SaleFirebaseMapper().transformModelToEntity(sale)
        dbSalesRef.child(saleFirebase.id.toString()).setValue(
            saleFirebase
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                liveData.value =
                    LiveDataStatusWrapper.success(SaleFirebaseMapper().transformEntityToModel(saleFirebase))
            } else {
                liveData.value =
                    LiveDataStatusWrapper.error("There was an error trying to make the sale with id-${saleFirebase.id}.", null)
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