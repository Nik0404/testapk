package com.acroninspector.app.data.repositories

import com.acroninspector.app.common.constants.FunctionNames
import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.data.datasource.database.dao.CheckListDao
import com.acroninspector.app.data.datasource.network.FunctionsApi
import com.acroninspector.app.data.util.constants.CheckListColumns
import com.acroninspector.app.data.util.filters.CheckListFilter
import com.acroninspector.app.data.util.mappers.CheckListMapper
import com.acroninspector.app.domain.entity.local.database.CheckList
import com.acroninspector.app.domain.entity.local.display.DisplayAnswer
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.domain.entity.remote.request.GettingDataRequest
import com.acroninspector.app.domain.entity.remote.request.other.Order
import com.acroninspector.app.domain.repositories.CheckListRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

class CheckListRepositoryImpl(
        private val functionsApi: FunctionsApi,
        private val checkListDao: CheckListDao,
        private val checkListMapper: CheckListMapper
) : CheckListRepository {

    override fun getQuestionIds(routeId: Int): Single<List<DisplayQuestion>> {
        return checkListDao.getQuestionsByRouteIdSingle(routeId)
    }

    override fun getCheckListsByRouteIdFromNetwork(sessionId: String, functionId: Int, routeId: Int): Flowable<List<CheckList>> {
        val order = listOf(
                Order(
                        CheckListColumns.ORDER_NUMBER_COLUMN_NAME,
                        NetworkConstants.ORDER_ASC
                )
        )
        val requestBody = GettingDataRequest(
                sessionId = sessionId,
                functionId = functionId,
                functionName = FunctionNames.CHECK_LISTS,
                columns = CheckListColumns.getColumns(),
                filter = CheckListFilter.getFilterByRouteId(routeId),
                order = order
        )

        return functionsApi.getData(requestBody)
                .map { response ->
                    val checkLists = mutableListOf<CheckList>()

                    response.values.forEach {
                        val checkListItem = checkListMapper.fromSchemaToEntity(it)
                        checkLists.add(checkListItem as CheckList)
                    }

                    checkLists.toList()
                }.toFlowable()
    }

    override fun getQuestionsByRouteId(routeId: Int): Flowable<List<DisplayQuestion>> {
        return checkListDao.getQuestionsByRouteId(routeId)
    }

    override fun getCheckListAttachmentsCount(checkListId: Int): Flowable<Int> {
        return checkListDao.getCheckListAttachmentsCountByCheckListId(checkListId)
    }

    override fun getAnswers(): Flowable<List<DisplayAnswer>> {
        return checkListDao.getAnswers()
    }

    override fun updateAnswer(checkListId: Int, answer: String, answerDate: String, isDefect: Boolean): Completable {
        return checkListDao.updateAnswer(checkListId, answer, answerDate, isDefect)
    }

    override fun getQuestionAnswer(checkListId: Int): Single<String> {
        return checkListDao.getQuestionAnswer(checkListId)
    }

    override fun updateComment(comment: String, checkListId: Int): Completable {
        return checkListDao.updateComment(comment, checkListId)
    }

    override fun checkIfCheckListExists(checkListId: Int): Maybe<Int> {
        return checkListDao.checkIfCheckListExists(checkListId)
    }
}
