package com.acroninspector.app.data.util.mappers

import com.acroninspector.app.data.util.constants.UserGroupColumns.USER_GROUP_FULL_NAME_POSITION
import com.acroninspector.app.data.util.constants.UserGroupColumns.USER_GROUP_ID_POSITION
import com.acroninspector.app.data.util.constants.UserGroupColumns.USER_GROUP_SHORT_NAME_POSITION
import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.domain.entity.local.database.AcronEntity
import com.acroninspector.app.domain.entity.local.database.UserGroup

class UserGroupMapper : EntityMapper<List<String>, AcronEntity> {

    override fun fromSchemaToEntity(scheme: List<String>): AcronEntity {
        return UserGroup(
                scheme[USER_GROUP_ID_POSITION].toInt(),
                scheme[USER_GROUP_FULL_NAME_POSITION],
                scheme[USER_GROUP_SHORT_NAME_POSITION]
        )
    }
}