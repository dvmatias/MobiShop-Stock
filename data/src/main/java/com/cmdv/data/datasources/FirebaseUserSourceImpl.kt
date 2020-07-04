package com.cmdv.data.datasources

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.datasources.FirebaseUserSource
import com.google.firebase.database.*

class FirebaseUserSourceImpl : FirebaseUserSource {

    private val dbRootRef: FirebaseDatabase = FirebaseDatabase.getInstance()

    private val dbWhiteListRef: DatabaseReference = dbRootRef.getReference("whitelist")

    /**
     *
     */
    override fun get(uid: String): MutableLiveData<Any> {
        TODO("Not yet implemented")
    }

    /**
     *
     */
    override fun isWhiteListed(email: String): MutableLiveData<Boolean> {
        val mutableLiveData = MutableLiveData<Boolean>()

        dbWhiteListRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var isWhitelisted = false
                for (element in snapshot.children) {
                    if (element.child("email").value == email) {
                        isWhitelisted = true
                        break
                    }
                }
                mutableLiveData.value = isWhitelisted
            }

            override fun onCancelled(error: DatabaseError) {
                mutableLiveData.value = false
            }
        })

        return mutableLiveData
    }

    /**
     *
     */
    override fun addToDb(user: Any) {
        TODO("Not yet implemented")
    }

}