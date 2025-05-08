package com.acroninspector.app.data.util.mappers

import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.domain.entity.local.database.UserFunction
import com.acroninspector.app.domain.entity.remote.schema.FunctionSchema

class FunctionMapper : EntityMapper<FunctionSchema, UserFunction> {

    override fun fromSchemaToEntity(scheme: FunctionSchema): UserFunction {
        return UserFunction(scheme.functionCode, scheme.functionName, scheme.functionTitle)
    }
}
