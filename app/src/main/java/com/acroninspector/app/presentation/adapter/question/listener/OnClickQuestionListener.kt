package com.acroninspector.app.presentation.adapter.question.listener

interface OnClickQuestionListener {

    fun onClickMenu()

    fun onClickAttachments(position: Int)

    fun onClickComment(position: Int)

    fun onClickEditDefect(position: Int)

    fun onClickSelectAnswer(position: Int, answerSelected: (String) -> Unit)

    fun updateAnswer(position: Int, answer: String)
}
