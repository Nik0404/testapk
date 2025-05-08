package com.acroninspector.app.domain.entity.local.other

import com.acroninspector.app.common.constants.Constants

class CheckListWithEquipment(
        val checkListId: Int,
        val equipmentId: Int,
        val typeAnswer: Int,
        val isDefect: Boolean,
        val comment: String
) {

    var answerDate: String = ""

    var criticality: Int = Constants.CRITICALITY_NO
    var attachmentsCount: Int = 0

    var answer: String = Constants.NO_ANSWER

    var minValue: Double = Constants.DEFAULT_MIN_VALUE
    var maxValue: Double = Constants.DEFAULT_MAX_VALUE
}