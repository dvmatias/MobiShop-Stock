package com.cmdv.domain.repositories

import androidx.lifecycle.MutableLiveData

interface UserRepository {

    fun get(uid: String): MutableLiveData<Any>

    fun isWhiteListed(email: String): MutableLiveData<Boolean>

    fun addToDb(user: Any)

}