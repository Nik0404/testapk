package com.acroninspector.app.presentation.fragment.userfunction

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.Functions
import com.acroninspector.app.common.utils.NetworkErrorsParser
import com.acroninspector.app.domain.entity.local.database.Division
import com.acroninspector.app.domain.entity.local.database.UserFunction
import com.acroninspector.app.domain.entity.local.display.DisplayUserFunction
import com.acroninspector.app.domain.interactors.login.LoginInteractor
import com.acroninspector.app.presentation.fragment.login.userfunction.UserFunctionPresenter
import com.acroninspector.app.presentation.fragment.login.userfunction.`UserFunctionView$$State`
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox
import java.lang.IllegalArgumentException

@RunWith(PowerMockRunner::class)
@PrepareForTest(NetworkErrorsParser::class)
class UserFunctionPresenterTest {

    private lateinit var presenter: UserFunctionPresenter

    private lateinit var networkErrorsParser: NetworkErrorsParser

    @Mock
    lateinit var interactor: LoginInteractor

    @Mock
    lateinit var viewState: `UserFunctionView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        networkErrorsParser = PowerMockito.mock(NetworkErrorsParser::class.java)

        presenter = UserFunctionPresenter(interactor, networkErrorsParser)
        presenter.setViewState(viewState)
    }

    private fun getFakeDivisions(): List<Division> {
        return arrayListOf(
                Division(0, "1"),
                Division(1, "2"),
                Division(2, "3")
        )
    }

    private fun getFakeDivision(): List<Division> {
        return arrayListOf(
                Division(0, "1")
        )
    }

    private fun getFakeUserFunctions(): List<UserFunction> {
        return arrayListOf(
                UserFunction(Functions.BYPASS, "ОбходТехнологическогоОборудования", "Обход Технологического Оборудования"),
                UserFunction(Functions.REGISTERING_LABELS, "РегистрацияМетокНаТехнологическомОборудовании", "Регистрация Меток На Технологическом Оборудовании"),
                UserFunction(Functions.BYPASS_MANAGEMENT, "УправлениеОбходамиТехнологическогоОборудования", "Управление Обходами Технологического Оборудования")
        )
    }

    private fun prepareFunctionsForShowing(functions: List<UserFunction>): List<DisplayUserFunction> {
        val resultFunctions = mutableListOf<DisplayUserFunction>()

        for (function in functions) {
            val functionShortTitle = getShortTitleForFunction(function)
            resultFunctions.add(DisplayUserFunction(function.functionCode, functionShortTitle))
        }

        return resultFunctions
    }

    private fun getShortTitleForFunction(function: UserFunction): Int {
        return when (function.functionCode) {
            Functions.BYPASS -> R.string.function_technologicl_equipment_bypass
            Functions.REGISTERING_LABELS -> R.string.function_registration_of_labels_on_the_equipment
            Functions.BYPASS_MANAGEMENT -> R.string.function_management_of_technological_equipment_bypasses
            else -> throw IllegalArgumentException("Unknown functionCode: ${function.functionCode}")
        }
    }

    @Test
    fun loadFunctions_Success() {
        val methodName = "loadFunctions"

        val userFunctions = getFakeUserFunctions()
        val userFunctionsWithShortTitle = prepareFunctionsForShowing(userFunctions)

        `when`(interactor.getFunctions())
                .thenReturn(Single.just(userFunctions))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(interactor).getFunctions()
        inOrder.verify(viewState).showFunctions(userFunctionsWithShortTitle)
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun loadFunctions_Failed() {
        val methodName = "loadFunctions"

        `when`(interactor.getFunctions())
                .thenReturn(Single.error(Throwable()))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(interactor).getFunctions()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)
        verify(viewState, never()).showFunctions(ArgumentMatchers.anyList())
    }

    @Test
    fun selectFunctionButtonClicked_WithCorrectFunctionCode_Success_WithSomeDivisions() {
        val divisions = getFakeDivisions()

        `when`(interactor.getDivisionsFromServer())
                .thenReturn(Single.just(divisions))

        presenter.selectFunctionButtonClicked(Functions.BYPASS)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getDivisionsFromServer()
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).openSupervisedUnitFragment()
        verify(viewState, never()).openMainScreen()
        verify(interactor, never()).saveSelectedDivisionId(ArgumentMatchers.anyInt())
    }

    @Test
    fun selectFunctionButtonClicked_WithCorrectFunctionCode_Success_WithOnlyOneDivision() {
        val divisions = getFakeDivision()

        `when`(interactor.getDivisionsFromServer())
                .thenReturn(Single.just(divisions))

        presenter.selectFunctionButtonClicked(Functions.BYPASS)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getDivisionsFromServer()
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(interactor).saveSelectedDivisionId(divisions[0].id)
        inOrder.verify(viewState).openMainScreen()
        verify(viewState, never()).openSupervisedUnitFragment()
    }

    @Test
    fun selectFunctionButtonClicked_WithCorrectFunctionCode_Success_WithEmptyDivisions() {
        val divisions = arrayListOf<Division>()

        `when`(interactor.getDivisionsFromServer())
                .thenReturn(Single.just(divisions))

        presenter.selectFunctionButtonClicked(Functions.BYPASS)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getDivisionsFromServer()
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).openMainScreen()
        verify(viewState, never()).openSupervisedUnitFragment()
        verify(interactor, never()).saveSelectedDivisionId(ArgumentMatchers.anyInt())
    }

    @Test
    fun selectFunctionButtonClicked_WithCorrectFunctionCode_Failed() {
        `when`(interactor.getDivisionsFromServer())
                .thenReturn(Single.error(Throwable()))

        presenter.selectFunctionButtonClicked(Functions.BYPASS)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getDivisionsFromServer()
        inOrder.verify(viewState).hideProgress()
        verify(viewState, never()).openSupervisedUnitFragment()
        verify(viewState, never()).openMainScreen()
        verify(interactor, never()).saveSelectedDivisionId(ArgumentMatchers.anyInt())

    }

    @Test
    fun selectFunctionButtonClicked_WithIncorrectFunctionCode() {
        presenter.selectFunctionButtonClicked(Constants.DEFAULT_INVALID_ID)

        verify(viewState, never()).showProgress()
    }

}