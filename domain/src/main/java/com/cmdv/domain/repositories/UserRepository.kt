package com.cmdv.domain.repositories

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.datasources.UserStoreListener
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.UserModel
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseUser

interface UserRepository {

    fun get(uid: String): MutableLiveData<Any>

    fun isWhiteListed(email: String): MutableLiveData<Boolean>

    fun storeUser(firebaseUser: FirebaseUser?, userStoreListener: UserStoreListener)

}