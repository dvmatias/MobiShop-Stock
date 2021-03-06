package com.cmdv.data.repositories

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.datasources.firebase.UserFirebaseSource
import com.cmdv.domain.datasources.firebase.UserStoreListener
import com.cmdv.domain.repositories.UserRepository
import com.google.firebase.auth.FirebaseUser

class UserRepositoryImpl(
    private val firebaseUserSource: UserFirebaseSource
) : UserRepository {

    override fun get(uid: String): MutableLiveData<Any> =
        firebaseUserSource.get(uid)

    override fun isWhiteListed(email: String): MutableLiveData<Boolean> =
        firebaseUserSource.isWhiteListed(email)

    override fun storeUser(firebaseUser: FirebaseUser?, userStoreListener: UserStoreListener) =
        firebaseUserSource.storeUser(firebaseUser, userStoreListener)

}