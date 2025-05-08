package com.acroninspector.app.presentation.fragment.supervisedunit

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.utils.NetworkErrorsParser
import com.acroninspector.app.domain.entity.local.database.Division
import com.acroninspector.app.domain.interactors.login.LoginInteractor
import com.acroninspector.app.presentation.fragment.login.supervisedunit.SupervisedUnitPresenter
import com.acroninspector.app.presentation.fragment.login.supervisedunit.`SupervisedUnitView$$State`
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

@RunWith(PowerMockRunner::class)
@PrepareForTest(NetworkErrorsParser::class)
class SupervisedUnitPresenterTest {

    private lateinit var presenter: SupervisedUnitPresenter

    private lateinit var networkErrorsParser: NetworkErrorsParser

    @Mock
    lateinit var interactor: LoginInteractor

    @Mock
    lateinit var viewState: `SupervisedUnitView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        networkErrorsParser = PowerMockito.mock(NetworkErrorsParser::class.java)

        presenter = SupervisedUnitPresenter(interactor, networkErrorsParser)
        presenter.setViewState(viewState)
    }

    private fun getFakeDivisions(): List<Division> {
        return arrayListOf(
                Division(0, "1"),
                Division(1, "2"),
                Division(2, "3")
        )
    }

    @Test
    fun loadDivisions_Success() {
        val methodName = "loadDivisions"
        val divisions = getFakeDivisions()

        `when`(interactor.getDivisions())
                .thenReturn(Single.just(getFakeDivisions()))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(interactor).getDivisions()
        inOrder.verify(viewState).showDivisions(divisions)

        verify(viewState, never()).showSnackbar(R.string.error_message)
    }

    @Test
    fun loadDivisions_Failed() {
        val methodName = "loadDivisions"

        `when`(interactor.getDivisions())
                .thenReturn(Single.error(Throwable()))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(interactor).getDivisions()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).showDivisions(ArgumentMatchers.anyList())
    }

    @Test
    fun selectDivisionButtonClicked_WithCorrectId() {
        val divisionId = 1

        presenter.selectDivisionButtonClicked(divisionId)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(interactor).saveSelectedDivisionId(divisionId)
        inOrder.verify(viewState).openMainScreen()

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun selectDivisionButtonClicked_WithIncorrectId() {
        presenter.selectDivisionButtonClicked(Constants.DEFAULT_INVALID_ID)

        verify(viewState, never()).openMainScreen()
    }
}