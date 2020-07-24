package com.cmdv.domain.datasources.firebase

import com.google.firebase.auth.FirebaseUser


interface AuthRegisterListener{

    fun onSuccess(firebaseUser: FirebaseUser?)

    fun onError(message: String)

}