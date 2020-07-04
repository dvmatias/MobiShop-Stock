package com.cmdv.domain.repositories

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.UserModel
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    fun login(email: String, password: String): MutableLiveData<LiveDataStatusWrapper<FirebaseUser>>

    fun register(email: String, password: String): MutableLiveData<LiveDataStatusWrapper<FirebaseUser>>

    fun logout()

    fun currentUser(): MutableLiveData<LiveDataStatusWrapper<UserModel?>>

}