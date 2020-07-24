package com.cmdv.domain.datasources.firebase

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.datasources.firebase.UserStoreListener
import com.google.firebase.auth.FirebaseUser

interface UserFirebaseSource {

    fun get(uid: String): MutableLiveData<Any>

    fun isWhiteListed(email: String): MutableLiveData<Boolean>

    fun storeUser(firebaseUser: FirebaseUser?, userStoreListener: UserStoreListener)

}