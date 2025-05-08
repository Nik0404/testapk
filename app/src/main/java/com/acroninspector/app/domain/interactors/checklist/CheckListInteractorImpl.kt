package com.acroninspector.app.domain.interactors.checklist

import com.acroninspector.app.common.constants.DatabaseConstants
import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.domain.entity.local.database.LocalDefect
import com.acroninspector.app.domain.entity.local.display.DisplayAnswer
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.domain.entity.local.display.DisplayRoute
import com.acroninspector.app.domain.repositories.CheckListRepository
import com.acroninspector.app.domain.repositories.LocalDefectRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.RouteRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.functions.BiFunction

class CheckListInteractorImpl(
        private val checkListRepository: CheckListRepository,
        private val localDefectRepository: LocalDefectRepository,
        private val routeRepository: RouteRepository,
        private val preferencesRepository: PreferencesRepository,
        private val schedulersProvider: SchedulersProvider
) : CheckListInteractor {

    override fun getQuestionsByRouteId(routeId: Int): Flowable<List<DisplayQuestion>> {
        return getQuestionsByRouteIdFromDatabase(routeId)
                .subscribeOn(schedulersProvider.io())
                .flatMap {
                    if (it.isEmpty()) {
                        getQuestionsByRouteIdFromNetwork(routeId)
                    } else Flowable.just(it)
                }.observeOn(schedulersProvider.ui())
    }

    private fun getQuestionsByRouteIdFromDatabase(routeId: Int): Flowable<List<DisplayQuestion>> {
        return Flowable.combineLatest(
                checkListRepository.getQuestionsByRouteId(routeId),
                checkListRepository.getAnswers(),
                BiFunction<List<DisplayQuestion>, List<DisplayAnswer>, List<DisplayQuestion>> { questions, answers ->
                    questions.forEach { displayQuestion ->
                        if (displayQuestion.typeAnswer == DatabaseConstants.TYPE_ANSWER_TEMPLATE) {
                            val answersByDefault = getAnswersByCheckListId(displayQuestion.id, answers)
                            displayQuestion.answersByDefault = answersByDefault
                        }
                    }
                    questions
                }).subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    private fun getAnswersByCheckListId(checkListId: Int, answers: List<DisplayAnswer>): List<DisplayAnswer> {
        val checkListAnswers: MutableList<DisplayAnswer> = mutableListOf()
        answers.forEach { answer ->
            if (answer.checkListId == checkListId) {
                checkListAnswers.add(answer)
            }
        }
        return checkListAnswers.toList()
    }

    private fun getQuestionsByRouteIdFromNetwork(routeId: Int): Flowable<List<DisplayQuestion>> {
        val sessionId = preferencesRepository.functionSessionId
        val functionId = preferencesRepository.functionId

        return checkListRepository.getCheckListsByRouteIdFromNetwork(sessionId, functionId, routeId)
                .map { checkLists ->
                    val displayQuestions: MutableList<DisplayQuestion> = mutableListOf()

                    checkLists.forEach { checkList ->
                        val displayQuestion = DisplayQuestion(
                                checkList.id, checkList.routeId, checkList.orderNumber,
                                checkList.questionText, checkList.typeAnswer, checkList.isDefect,
                                checkList.userComment
                        )
                        displayQuestion.attachmentsCount = checkList.attachmentsCount
                        displayQuestion.answerDate = checkList.endDate
                        displayQuestion.minValue = checkList.minValue
                        displayQuestion.maxValue = checkList.maxValue
                        displayQuestion.answer = checkList.value

                        displayQuestions.add(displayQuestion)
                    }

                    displayQuestions.toList()
                }.onErrorResumeNext(Flowable.just(arrayListOf()))
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun getRouteById(routeId: Int): Flowable<DisplayRoute> {
        return routeRepository.getRouteById(routeId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun getDefectLogByCheckListId(checkListId: Int): Maybe<DisplayDefectLog> {
        return localDefectRepository.getDisplayDefectLogByCheckListId(checkListId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun updateAnswer(
            routeId: Int,
            questionId: Int,
            answer: String,
            answerDate: String,
            isDefect: Boolean
    ): Maybe<LocalDefect> {
        return checkListRepository.getQuestionAnswer(questionId)
                .flatMapCompletable { currentAnswer ->
                    //Updating route.count_answer if needed
                    if (currentAnswer != answer) {
                        val unansweredListsCompletable = when {
                            currentAnswer.isEmpty() && answer.isNotEmpty() -> {
                                //Answer has been added
                                routeRepository.increaseUnansweredLists(routeId)
                            }
                            currentAnswer.isNotEmpty() && answer.isEmpty() -> {
                                //Answer has been cleared
                                routeRepository.decreaseUnansweredLists(routeId)
                            }
                            else -> Completable.complete()
                        }

                        Completable.concatArray(
                                checkListRepository.updateAnswer(questionId, answer, answerDate, isDefect),
                                routeRepository.updateEndDateActual(routeId, answerDate),
                                unansweredListsCompletable
                        )
                    } else Completable.complete()
                }.andThen(localDefectRepository.getLocalDefectByCheckListId(questionId))
                .doOnComplete { preferencesRepository.isDataUploadedToServer = false }
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun insertLocalDefect(localDefect: LocalDefect): Completable {
        return localDefectRepository.insertLocalDefect(localDefect)
                .doOnComplete { preferencesRepository.isDataUploadedToServer = false }
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun updateQuestionComment(checkListId: Int, comment: String): Completable {
        return Completable.concatArray(
                checkListRepository.updateComment(comment, checkListId),
                localDefectRepository.updateCommentByCheckListId(comment, checkListId)
        ).subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}