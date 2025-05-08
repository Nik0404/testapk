package com.acroninspector.app.data.util.mappers.base

import com.acroninspector.app.common.constants.FunctionNames
import com.acroninspector.app.data.util.mappers.AnswerMapper
import com.acroninspector.app.data.util.mappers.AttachmentMapper
import com.acroninspector.app.data.util.mappers.CheckListMapper
import com.acroninspector.app.data.util.mappers.DefectCauseMapper
import com.acroninspector.app.data.util.mappers.DefectLogMapper
import com.acroninspector.app.data.util.mappers.DefectMapper
import com.acroninspector.app.data.util.mappers.DefectRelationsMapper
import com.acroninspector.app.data.util.mappers.DirectoryMapper
import com.acroninspector.app.data.util.mappers.EquipmentClassMapper
import com.acroninspector.app.data.util.mappers.EquipmentMapper
import com.acroninspector.app.data.util.mappers.ExecutorMapper
import com.acroninspector.app.data.util.mappers.NfcEquipmentMapper
import com.acroninspector.app.data.util.mappers.NfcRouteMapper
import com.acroninspector.app.data.util.mappers.RouteMapper
import com.acroninspector.app.data.util.mappers.TaskMapper
import com.acroninspector.app.data.util.mappers.UserGroupMapper
import com.acroninspector.app.domain.entity.local.database.AcronEntity

class MapperFactory {

    fun createMapperByFunctionName(functionName: String): EntityMapper<List<String>, AcronEntity> {
        return when (functionName) {
            FunctionNames.DIRECTORIES -> DirectoryMapper()
            FunctionNames.TASKS -> TaskMapper()
            FunctionNames.EQUIPMENT_CLASS -> EquipmentClassMapper()
            FunctionNames.EQUIPMENT -> EquipmentMapper()
            FunctionNames.NFC_EQUIPMENT -> NfcEquipmentMapper()
            FunctionNames.NFC_ROUTE -> NfcRouteMapper()
            FunctionNames.ROUTES -> RouteMapper()
            FunctionNames.CHECK_LISTS -> CheckListMapper()
            FunctionNames.EXECUTORS -> ExecutorMapper()
            FunctionNames.DEFECT_CAUSES -> DefectCauseMapper()
            FunctionNames.DEFECTS -> DefectMapper()
            FunctionNames.USER_GROUPS -> UserGroupMapper()
            FunctionNames.DEFECT_RELATIONS -> DefectRelationsMapper()
            FunctionNames.ANSWER -> AnswerMapper()
            FunctionNames.ATTACHMENTS -> AttachmentMapper()
            FunctionNames.DEFECT_LOG -> DefectLogMapper()

            else -> throw IllegalArgumentException("Unknown function name: $functionName")
        }
    }
}