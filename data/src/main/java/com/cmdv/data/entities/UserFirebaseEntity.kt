package com.cmdv.data.entities

class UserFirebaseEntity(
    val uid: String?,
    val name: String?,
    val email: String?,
    val isAdmin: Boolean?,
    val canRead: Boolean?,
    val canWrite: Boolean?,
    val date: DateEntity?
) {

    @Suppress("unused")
    constructor() : this(null, null, null, null, null, null, null)

}