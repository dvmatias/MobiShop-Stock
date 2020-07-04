package com.cmdv.domain.datasources

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.UserModel
import com.google.firebase.auth.FirebaseUser

interface FirebaseAuthSource {

    fun register(email: String, password: String, authRegisterListener: AuthRegisterListener)

    fun login(email: String, password: String): MutableLiveData<LiveDataStatusWrapper<FirebaseUser>>

    fun currentUser():  MutableLiveData<LiveDataStatusWrapper<UserModel?>>

    fun logout()

}

interface AuthRegisterListener{
    fun onSuccess(firebaseUser: FirebaseUser?)
    fun onError(message: String)
}