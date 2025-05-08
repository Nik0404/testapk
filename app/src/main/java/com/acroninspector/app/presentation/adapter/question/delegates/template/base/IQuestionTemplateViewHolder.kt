package com.acroninspector.app.presentation.adapter.question.delegates.template.base

import com.acroninspector.app.presentation.adapter.question.delegates.base.IBaseQuestionViewHolder

interface IQuestionTemplateViewHolder : IBaseQuestionViewHolder {

    fun hasAnswer(): Boolean
}