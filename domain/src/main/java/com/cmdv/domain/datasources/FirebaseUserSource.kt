package com.cmdv.domain.datasources

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.UserModel
import com.google.firebase.auth.FirebaseUser

interface FirebaseUserSource {

    fun get(uid: String): MutableLiveData<Any>

    fun isWhiteListed(email: String): MutableLiveData<Boolean>

    fun storeUser(firebaseUser: FirebaseUser?, userStoreListener: UserStoreListener)

}

interface UserStoreListener{
    fun onSuccess(user: UserModel)
    fun onError(message: String)
}