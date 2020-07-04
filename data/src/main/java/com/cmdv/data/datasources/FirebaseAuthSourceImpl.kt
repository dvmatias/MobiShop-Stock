package com.cmdv.data.datasources

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.datasources.FirebaseAuthSource
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

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

    override fun currentUser(): MutableLiveData<LiveDataStatusWrapper<UserModel?>> {
        val mutableLiveData = MutableLiveData<LiveDataStatusWrapper<UserModel?>>()
        if (firebaseAuth.currentUser != null) {
            val currentUser: FirebaseUser = firebaseAuth.currentUser!!
            mutableLiveData.value =
                LiveDataStatusWrapper.success(UserModel(currentUser.uid, currentUser.displayName ?: "", currentUser.email ?: ""))
        } else {
            mutableLiveData.value =
                LiveDataStatusWrapper.error("User not logged in", null)
        }
        return mutableLiveData
    }


}