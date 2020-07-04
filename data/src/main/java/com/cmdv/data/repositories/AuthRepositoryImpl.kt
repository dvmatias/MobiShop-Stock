package com.cmdv.data.repositories

import androidx.lifecycle.MutableLiveData
import com.cmdv.domain.datasources.AuthRegisterListener
import com.cmdv.domain.datasources.FirebaseAuthSource
import com.cmdv.domain.models.LiveDataStatusWrapper
import com.cmdv.domain.models.UserModel
import com.cmdv.domain.repositories.AuthRepository
import com.google.firebase.auth.FirebaseUser

class AuthRepositoryImpl(
    private val firebaseAuthSource: FirebaseAuthSource
) : AuthRepository {

    override fun register(
        email: String,
        password: String,
        authRegisterListener: AuthRegisterListener
    ) = firebaseAuthSource.register(email, password, authRegisterListener)

    override fun login(email: String, password: String): MutableLiveData<LiveDataStatusWrapper<FirebaseUser>> =
        firebaseAuthSource.login(email, password)

    override fun currentUser():  MutableLiveData<LiveDataStatusWrapper<UserModel?>> =
        firebaseAuthSource.currentUser()

    override fun logout() =
        firebaseAuthSource.logout()
}