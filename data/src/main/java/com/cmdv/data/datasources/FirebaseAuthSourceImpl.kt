package com.cmdv.data.datasources

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.datasources.FirebaseAuthSource
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class FirebaseAuthSourceImpl : FirebaseAuthSource {

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

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                mutableLiveData.value = LiveDataStatusWrapper.success(firebaseAuth.currentUser)
            } else {
                mutableLiveData.value = LiveDataStatusWrapper.error(it.exception?.message ?: "", null)
            }
        }

        return mutableLiveData
    }

    override fun logout() =
        firebaseAuth.signOut()

    override fun currentUser(): FirebaseUser? =
        firebaseAuth.currentUser

}