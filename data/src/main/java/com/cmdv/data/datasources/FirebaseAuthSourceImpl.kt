package com.cmdv.data.datasources

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.datasources.FirebaseAuthSource
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class FirebaseAuthSourceImpl : FirebaseAuthSource {

    private val dbRootRef: FirebaseDatabase = FirebaseDatabase.getInstance()

    private val dbWhiteListRef: DatabaseReference = dbRootRef.getReference("whitelist")

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun login(email: String, password: String): MutableLiveData<LiveDataStatusWrapper<FirebaseUser>> {
        val mutableLiveData = MutableLiveData<LiveDataStatusWrapper<FirebaseUser>>()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                mutableLiveData.value = LiveDataStatusWrapper.success(firebaseAuth.currentUser)
            } else {
                mutableLiveData.value = LiveDataStatusWrapper.error(it.exception?.message ?: "", null)
            }
        }
        return mutableLiveData
    }

    override fun register(email: String, password: String): MutableLiveData<LiveDataStatusWrapper<FirebaseUser>> {
        val mutableLiveData = MutableLiveData<LiveDataStatusWrapper<FirebaseUser>>()

        dbWhiteListRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var isWhiteListed: Boolean = false
                for (element in snapshot.children) {
                    if (element.value == email)
                        isWhiteListed = true
                }
                if (isWhiteListed) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            mutableLiveData.value = LiveDataStatusWrapper.success(firebaseAuth.currentUser)
                        } else {
                            mutableLiveData.value = LiveDataStatusWrapper.error(it.exception?.message ?: "", null)
                        }
                    }
                } else {
                    mutableLiveData.value = LiveDataStatusWrapper.error("Sorry, this email seems to be invalid. Please contact the support team for more information.", null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                mutableLiveData.value = LiveDataStatusWrapper.error(error.message, null)
            }
        })

        return mutableLiveData
    }

    override fun logout() =
        firebaseAuth.signOut()

    override fun currentUser(): FirebaseUser? =
        firebaseAuth.currentUser

}