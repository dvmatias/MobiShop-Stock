package com.cmdv.data.mappers

import com.cmdv.data.entities.UserFirebaseEntity
import com.cmdv.domain.mapper.BaseMapper
import com.cmdv.domain.models.DateModel
import com.cmdv.domain.models.UserModel

class UserMapper : BaseMapper<UserFirebaseEntity, UserModel>() {

    override fun transformEntityToModel(e: UserFirebaseEntity): UserModel {
        return UserModel(
            e.uid ?: "",
            e.name ?: "",
            e.email ?: "",
            e.isAdmin ?: false,
            e.canRead ?: false,
            e.canWrite ?: false,
            DateModel(
                e.date?.createdDate ?: "",
                e.date?.updatedDate ?: ""
            )
        )
    }
}