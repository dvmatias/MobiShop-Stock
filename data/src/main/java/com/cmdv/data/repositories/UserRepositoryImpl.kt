package com.cmdv.data.repositories

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.datasources.FirebaseUserSource
import com.cmdv.domain.repositories.UserRepository

class UserRepositoryImpl(
    private val firebaseUserSource: FirebaseUserSource
) : UserRepository {

    override fun get(uid: String): MutableLiveData<Any> =
        firebaseUserSource.get(uid)

    override fun isWhiteListed(email: String): MutableLiveData<Boolean> =
        firebaseUserSource.isWhiteListed(email)

    override fun addToDb(user: Any) =
        firebaseUserSource.addToDb(user)

}