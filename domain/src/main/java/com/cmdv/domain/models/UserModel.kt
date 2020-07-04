package com.cmdv.domain.models

data class UserModel(
    val uid: String,
    val name: String,
    val email: String,
    val isAdmin: Boolean,
    val canRead: Boolean,
    val canWrite: Boolean,
    val date: DateModel
)