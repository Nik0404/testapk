package com.acroninspector.app.presentation.adapter.question.delegates.value.base

import com.acroninspector.app.presentation.adapter.question.delegates.base.IBaseQuestionViewHolder

interface IQuestionValueViewHolder : IBaseQuestionViewHolder {

    fun setEditDefectVisibility(isDefect: Boolean)

    fun isDefect(answer: String): Boolean

    fun hasAnswer(): Boolean
}