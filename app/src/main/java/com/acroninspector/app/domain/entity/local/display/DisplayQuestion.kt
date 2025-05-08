package com.acroninspector.app.domain.entity.local.display

import androidx.room.Ignore
import com.acroninspector.app.common.constants.Constants

class DisplayQuestion(
        val id: Int,
        val routeId: Int,
        val orderNumber: Int,
        val question: String,
        val typeAnswer: Int,
        var isDefect: Boolean,
        var comment: String
) {

    var answer: String = Constants.NO_ANSWER
    var answerDate: String = ""
    var simbolsAfterComma: Int = 0

    var criticality: Int = Constants.CRITICALITY_NO
    var attachmentsCount: Int = 0

    var defectNameId: Int = Constants.DEFAULT_INVALID_ID
    val hasDefectName: Boolean
        get() = defectNameId != Constants.DEFAULT_INVALID_ID && defectNameId > 0

    var minValue: Double = Constants.DEFAULT_MIN_VALUE
    var maxValue: Double = Constants.DEFAULT_MAX_VALUE

    @Ignore
    var answersByDefault: List<DisplayAnswer> = listOf()
}