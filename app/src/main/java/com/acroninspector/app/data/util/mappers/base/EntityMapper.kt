package com.acroninspector.app.data.util.mappers.base

interface EntityMapper<S, E> {

    fun fromSchemaToEntity(scheme: S): E
}
