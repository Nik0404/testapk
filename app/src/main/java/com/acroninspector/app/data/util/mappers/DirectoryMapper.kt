package com.acroninspector.app.data.util.mappers

import com.acroninspector.app.common.extension.toId
import com.acroninspector.app.data.util.constants.DirectoryColumns
import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.domain.entity.local.database.AcronEntity
import com.acroninspector.app.domain.entity.local.database.Directory

class DirectoryMapper : EntityMapper<List<String>, AcronEntity> {

    override fun fromSchemaToEntity(scheme: List<String>): AcronEntity {
        return Directory(
            scheme[DirectoryColumns.ID_COLUMN_POSITION].toId(),
            scheme[DirectoryColumns.NAME_COLUMN_POSITION],
            scheme[DirectoryColumns.SIGN_COLUMN_POSITION],
            getParentIdForDirectorySchema(scheme),
            scheme[DirectoryColumns.END_LVL_COLUMN_POSITION].toBoolean()
        )
    }

    private fun getParentIdForDirectorySchema(scheme: List<String>): Int {
        return if (scheme[DirectoryColumns.PARENT_ID_COLUMN_POSITION].isNotEmpty()) {
            scheme[DirectoryColumns.PARENT_ID_COLUMN_POSITION].toId()
        } else -1
    }
}
