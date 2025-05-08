package com.acroninspector.app.domain.interactors.checklist

import com.acroninspector.app.domain.entity.local.database.LocalDefect
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.domain.entity.local.display.DisplayRoute
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

interface CheckListInteractor {

    fun getRouteById(routeId: Int): Flowable<DisplayRoute>

    fun getQuestionsByRouteId(routeId: Int): Flowable<List<DisplayQuestion>>

    fun getDefectLogByCheckListId(checkListId: Int): Maybe<DisplayDefectLog>

    fun updateAnswer(
            routeId: Int,
            questionId: Int,
            answer: String,
            answerDate: String,
            isDefect: Boolean
    ): Maybe<LocalDefect>

    fun insertLocalDefect(localDefect: LocalDefect): Completable

    fun updateQuestionComment(checkListId: Int, comment: String): Completable
}