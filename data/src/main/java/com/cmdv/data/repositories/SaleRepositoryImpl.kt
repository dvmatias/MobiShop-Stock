package com.cmdv.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cmdv.data.BuildConfig
import com.cmdv.data.entities.firebase.SaleFirebaseEntity
import com.cmdv.data.mappers.SaleFirebaseMapper
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.SaleModel
import com.cmdv.domain.repositories.SaleRepository
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SaleRepositoryImpl : SaleRepository {

    private val dbRootRef: FirebaseDatabase = FirebaseDatabase.getInstance()

    private val dbSalesRef: DatabaseReference = dbRootRef.getReference(BuildConfig.DB_SALES_ROOT_PATH)

    override fun createSale(sale: SaleModel): MutableLiveData<LiveDataStatusWrapper<SaleModel>> {
        val mutableLiveData = MutableLiveData<LiveDataStatusWrapper<SaleModel>>()
        mutableLiveData.value = LiveDataStatusWrapper.loading(null)

        val saleFirebase: SaleFirebaseEntity =
            SaleFirebaseMapper().transformModelToEntity(sale)
        dbSalesRef.child(saleFirebase.id.toString()).setValue(
            saleFirebase
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mutableLiveData.value =
                    LiveDataStatusWrapper.success(
                        SaleFirebaseMapper().transformEntityToModel(saleFirebase)
                    )
            } else {
                mutableLiveData.value =
                    LiveDataStatusWrapper.error("There was an error trying to make the sale with id-${saleFirebase.id}.", null)
            }
        }

        return mutableLiveData
    }
}