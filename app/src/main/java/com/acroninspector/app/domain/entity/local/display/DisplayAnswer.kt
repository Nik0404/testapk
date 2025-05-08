package com.acroninspector.app.domain.entity.local.display

data class DisplayAnswer(
    val id: Int,
    val checkListId: Int,
    val orderNumber: Int,
    val answerText: String,
    val isDefect: Boolean,
    val isDefault: Boolean
)