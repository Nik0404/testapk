package com.acroninspector.app.data.util.mappers

import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.domain.entity.local.database.Division
import com.acroninspector.app.domain.entity.remote.schema.DivisionSchema

class DivisionMapper : EntityMapper<DivisionSchema, Division> {

    override fun fromSchemaToEntity(scheme: DivisionSchema): Division {
        return Division(scheme.divisionId, scheme.divisionName)
    }
}
