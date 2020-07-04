package com.cmdv.domain.datasources

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.google.firebase.auth.FirebaseUser

interface FirebaseAuthSource {

    fun login(email: String, password: String): MutableLiveData<LiveDataStatusWrapper<FirebaseUser>>

    fun register(email: String, password: String): MutableLiveData<LiveDataStatusWrapper<FirebaseUser>>

    fun logout()

    fun currentUser(): FirebaseUser?

}