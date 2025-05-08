package com.acroninspector.app.domain.repositories

import com.acroninspector.app.domain.entity.local.database.CheckList
import com.acroninspector.app.domain.entity.local.display.DisplayAnswer
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface CheckListRepository {

    fun getCheckListsByRouteIdFromNetwork(sessionId: String, functionId: Int, routeId: Int): Flowable<List<CheckList>>

    fun getCheckListAttachmentsCount(checkListId: Int): Flowable<Int>

    fun getQuestionsByRouteId(routeId: Int): Flowable<List<DisplayQuestion>>

    fun getQuestionIds(routeId: Int): Single<List<DisplayQuestion>>

    fun getAnswers(): Flowable<List<DisplayAnswer>>

    fun updateComment(comment: String, checkListId: Int): Completable

    fun updateAnswer(checkListId: Int, answer: String, answerDate: String, isDefect: Boolean): Completable

    fun getQuestionAnswer(checkListId: Int): Single<String>

    fun checkIfCheckListExists(checkListId: Int): Maybe<Int>
}
