package com.cmdv.domain.datasources.firebase

import com.cmdv.domain.models.UserModel

interface UserStoreListener {

    fun onSuccess(user: UserModel)

    fun onError(message: String)

}