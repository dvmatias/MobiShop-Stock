package com.cmdv.data.datasources

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.datasources.AuthRegisterListener
import com.cmdv.domain.datasources.FirebaseAuthSource
import com.cmdv.domain.models.DateModel
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthSourceImpl : FirebaseAuthSource {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun register(email: String, password: String, authRegisterListener: AuthRegisterListener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                authRegisterListener.onSuccess(firebaseAuth.currentUser)
//                mutableLiveData.value = LiveDataStatusWrapper.success(firebaseAuth.currentUser)
            } else {
                authRegisterListener.onError(it.exception?.message ?: "")
//                mutableLiveData.value = LiveDataStatusWrapper.error(it.exception?.message ?: "", null)
            }
        }
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

    override fun currentUser(): MutableLiveData<LiveDataStatusWrapper<UserModel?>> {
        val mutableLiveData = MutableLiveData<LiveDataStatusWrapper<UserModel?>>()
        if (firebaseAuth.currentUser != null) {
            val currentUser: FirebaseUser = firebaseAuth.currentUser!!
            // TODO
            mutableLiveData.value =
                LiveDataStatusWrapper.success(
                    UserModel(
                        currentUser.uid,
                        currentUser.displayName ?: "",
                        currentUser.email ?: "",
                    false,
                    false,
                    false,
                    DateModel("", "")
                    )
                )
        } else {
            mutableLiveData.value =
                LiveDataStatusWrapper.error("User not logged in", null)
        }
        return mutableLiveData
    }

    override fun logout() =
        firebaseAuth.signOut()

}