package com.acroninspector.app.presentation.fragment.login

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Functions
import com.acroninspector.app.common.utils.NetworkErrorsParser
import com.acroninspector.app.domain.entity.local.database.Division
import com.acroninspector.app.domain.entity.local.database.UserFunction
import com.acroninspector.app.domain.interactors.login.LoginInteractor
import com.acroninspector.app.presentation.fragment.login.auth.LoginPresenter
import com.acroninspector.app.presentation.fragment.login.auth.`LoginView$$State`
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox
import retrofit2.Response


@RunWith(PowerMockRunner::class)
@PrepareForTest(NetworkErrorsParser::class)
class LoginPresenterTest {

    private lateinit var presenter: LoginPresenter

    private lateinit var networkErrorsParser: NetworkErrorsParser

    @Mock
    lateinit var interactor: LoginInteractor

    @Mock
    lateinit var viewState: `LoginView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        networkErrorsParser = PowerMockito.mock(NetworkErrorsParser::class.java)

        presenter = LoginPresenter(interactor, networkErrorsParser)
        presenter.setViewState(viewState)
    }

    private fun getFakeHttpException(): HttpException {
        val responseBody =
                getFakeErrorMessage().toResponseBody("application/json".toMediaTypeOrNull())
        val response = Response.error<Any>(400, responseBody)
        return HttpException(response)
    }

    private fun getFakeErrorMessage(): String {
        return "error"
    }

    private fun getFakeLogin(): String {
        return "login"
    }

    private fun getFakePassword(): String {
        return "password"
    }

    private fun getFakeUserFunctions(): List<UserFunction> {
        return arrayListOf(
                UserFunction(Functions.BYPASS, "ОбходТехнологическогоОборудования", "Обход Технологического Оборудования"),
                UserFunction(Functions.REGISTERING_LABELS, "РегистрацияМетокНаТехнологическомОборудовании", "Регистрация Меток На Технологическом Оборудовании"),
                UserFunction(Functions.BYPASS_MANAGEMENT, "УправлениеОбходамиТехнологическогоОборудования", "Управление Обходами Технологического Оборудования")
        )
    }

    private fun getFakeUserFunction(): List<UserFunction> {
        return arrayListOf(
                UserFunction(Functions.BYPASS, "ОбходТехнологическогоОборудования", "Обход Технологического Оборудования")
        )
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

    @Test
    fun onLoginButtonClicked_Failed_WithHttpException() {
        val login = getFakeLogin()
        val password = getFakePassword()

        val error = getFakeErrorMessage()

        PowerMockito.`when`(networkErrorsParser.parseErrorMessage(error))
                .thenReturn(getFakeErrorMessage())

        `when`(interactor.loginUser(login, password,false))
                .thenReturn(Single.error(getFakeHttpException()))

        presenter.onLoginButtonClicked(login, password)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).loginUser(login, password, false)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(getFakeErrorMessage())

        verify(viewState, never()).showSnackbar(R.string.error_message)
        verify(viewState, never()).openUserFunctionsScreen()
        verify(viewState, never()).openSupervisedUnitFragment()
        verify(viewState, never()).openMainScreen()
        verify(interactor, never()).saveSelectedFunctionId(ArgumentMatchers.anyInt())
        verify(interactor, never()).saveSelectedDivisionId(ArgumentMatchers.anyInt())
        verify(interactor, never()).getDivisionsFromServer()
    }

    @Test
    fun onLoginButtonClicked_Failed_WithSomeException() {
        val login = getFakeLogin()
        val password = getFakePassword()

        `when`(interactor.loginUser(login, password, false))
                .thenReturn(Single.error(Throwable()))

        presenter.onLoginButtonClicked(login, password)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).loginUser(login, password, false)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).showSnackbar(getFakeErrorMessage())
        verify(viewState, never()).openUserFunctionsScreen()
        verify(viewState, never()).openSupervisedUnitFragment()
        verify(viewState, never()).openMainScreen()
        verify(interactor, never()).saveSelectedFunctionId(ArgumentMatchers.anyInt())
        verify(interactor, never()).saveSelectedDivisionId(ArgumentMatchers.anyInt())
        verify(interactor, never()).getDivisionsFromServer()
    }

    @Test
    fun onLoginButtonClickedSuccess_WithSomeUserFunctions() {
        val login = getFakeLogin()
        val password = getFakePassword()

        val userFunctions = getFakeUserFunctions()

        `when`(interactor.loginUser(login, password, false))
                .thenReturn(Single.just(userFunctions))

        presenter.onLoginButtonClicked(login, password)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).loginUser(login, password, false)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).openUserFunctionsScreen()

        verify(viewState, never()).showSnackbar(R.string.error_message)
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyString())
        verify(viewState, never()).openSupervisedUnitFragment()
        verify(viewState, never()).openMainScreen()
        verify(interactor, never()).saveSelectedFunctionId(ArgumentMatchers.anyInt())
        verify(interactor, never()).saveSelectedDivisionId(ArgumentMatchers.anyInt())
        verify(interactor, never()).getDivisionsFromServer()
    }

    @Test
    fun onLoginButtonClickedSuccess_WithEmptyUserFunctions() {
        val login = getFakeLogin()
        val password = getFakePassword()

        val userFunctions = arrayListOf<UserFunction>()

        `when`(interactor.loginUser(login, password, false))
                .thenReturn(Single.just(userFunctions))

        presenter.onLoginButtonClicked(login, password)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).loginUser(login, password, false)
        inOrder.verify(viewState).hideProgress()

        verify(viewState, never()).showSnackbar(R.string.error_message)
        verify(viewState, never()).openUserFunctionsScreen()
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyString())
        verify(viewState, never()).openSupervisedUnitFragment()
        verify(viewState, never()).openMainScreen()
        verify(interactor, never()).saveSelectedFunctionId(ArgumentMatchers.anyInt())
        verify(interactor, never()).saveSelectedDivisionId(ArgumentMatchers.anyInt())
        verify(interactor, never()).getDivisionsFromServer()
    }

    @Test
    fun onLoginButtonClickedSuccess_WithOnlyOneUserFunction() {
        val login = getFakeLogin()
        val password = getFakePassword()

        val userFunctions = getFakeUserFunction()
        val divisions = getFakeDivisions()

        `when`(interactor.getDivisionsFromServer())
                .thenReturn(Single.just(divisions))

        `when`(interactor.loginUser(login, password, false))
                .thenReturn(Single.just(userFunctions))

        presenter.onLoginButtonClicked(login, password)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).loginUser(login, password, false)
        inOrder.verify(interactor).saveSelectedFunctionId(ArgumentMatchers.anyInt())

        verify(viewState, never()).openUserFunctionsScreen()
        verify(viewState, never()).showSnackbar(R.string.error_message)
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyString())
        verify(viewState, never()).openMainScreen()
        verify(interactor, never()).saveSelectedDivisionId(ArgumentMatchers.anyInt())
    }

    @Test
    fun getDivisionsFromServer_Failed_WithHttpException() {
        val methodName = "getDivisionsFromServer"
        val error = getFakeErrorMessage()

        PowerMockito.`when`(networkErrorsParser.parseErrorMessage(error))
                .thenReturn(getFakeErrorMessage())

        `when`(interactor.getDivisionsFromServer())
                .thenReturn(Single.error(getFakeHttpException()))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(interactor).getDivisionsFromServer()
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(getFakeErrorMessage())

        verify(viewState, never()).openMainScreen()
        verify(interactor, never()).saveSelectedDivisionId(ArgumentMatchers.anyInt())
        verify(viewState, never()).openSupervisedUnitFragment()
        verify(viewState, never()).openUserFunctionsScreen()
        verify(viewState, never()).showSnackbar(R.string.error_message)
    }

    @Test
    fun getDivisionsFromServer_Failed_WithSomeException() {
        val methodName = "getDivisionsFromServer"

        `when`(interactor.getDivisionsFromServer())
                .thenReturn(Single.error(Throwable()))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(interactor).getDivisionsFromServer()
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).openMainScreen()
        verify(interactor, never()).saveSelectedDivisionId(ArgumentMatchers.anyInt())
        verify(viewState, never()).openSupervisedUnitFragment()
        verify(viewState, never()).openUserFunctionsScreen()
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyString())
    }

    @Test
    fun getDivisionsFromServer_Success_WithSomeDivisions() {
        val methodName = "getDivisionsFromServer"
        val divisions = getFakeDivisions()

        `when`(interactor.getDivisionsFromServer())
                .thenReturn(Single.just(divisions))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(interactor).getDivisionsFromServer()
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).openSupervisedUnitFragment()

        verify(interactor, never()).saveSelectedDivisionId(ArgumentMatchers.anyInt())
        verify(viewState, never()).openMainScreen()
        verify(viewState, never()).openUserFunctionsScreen()
        verify(viewState, never()).showSnackbar(R.string.error_message)
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyString())
    }

    @Test
    fun getDivisionsFromServer_Success_WithEmptyDivisions() {
        val methodName = "getDivisionsFromServer"
        val divisions = arrayListOf<Division>()

        `when`(interactor.getDivisionsFromServer())
                .thenReturn(Single.just(divisions))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(interactor).getDivisionsFromServer()
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).openMainScreen()

        verify(interactor, never()).saveSelectedDivisionId(ArgumentMatchers.anyInt())
        verify(viewState, never()).openSupervisedUnitFragment()
        verify(viewState, never()).openUserFunctionsScreen()
        verify(viewState, never()).showSnackbar(R.string.error_message)
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyString())
    }

    @Test
    fun getDivisionsFromServer_Success_WithOnlyOneDivision() {
        val methodName = "getDivisionsFromServer"
        val divisions = getFakeDivision()

        `when`(interactor.getDivisionsFromServer())
                .thenReturn(Single.just(divisions))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(interactor).getDivisionsFromServer()
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(interactor).saveSelectedDivisionId(divisions[0].id)
        inOrder.verify(viewState).openMainScreen()

        verify(viewState, never()).openSupervisedUnitFragment()
        verify(viewState, never()).openUserFunctionsScreen()
        verify(viewState, never()).showSnackbar(R.string.error_message)
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyString())
    }

    @Test
    fun onLoginButtonClicked_WithEmptyLogin() {
        val login = ""
        val password = getFakePassword()

        presenter.onLoginButtonClicked(login, password)

        verify(viewState).showSnackbar(R.string.login_fields_empty)
        verify(viewState, never()).showProgress()
        verify(viewState, never()).hideProgress()
        verify(viewState, never()).openUserFunctionsScreen()
        verify(viewState, never()).showSnackbar(R.string.error_message)
        verify(interactor, never()).saveSelectedFunctionId(ArgumentMatchers.anyInt())
    }

    @Test
    fun onLoginButtonClicked_WithEmptyPassword() {
        val login = getFakeLogin()
        val password = ""

        presenter.onLoginButtonClicked(login, password)

        verify(viewState).showSnackbar(R.string.login_fields_empty)
        verify(viewState, never()).showProgress()
        verify(viewState, never()).hideProgress()
        verify(viewState, never()).openUserFunctionsScreen()
        verify(viewState, never()).showSnackbar(R.string.error_message)
        verify(interactor, never()).saveSelectedFunctionId(ArgumentMatchers.anyInt())
    }

    @Test
    fun onLoginButtonClicked_WithEmptyBoth() {
        val login = ""
        val password = ""

        presenter.onLoginButtonClicked(login, password)

        verify(viewState).showSnackbar(R.string.login_fields_empty)
        verify(viewState, never()).showProgress()
        verify(viewState, never()).hideProgress()
        verify(viewState, never()).openUserFunctionsScreen()
        verify(viewState, never()).showSnackbar(R.string.error_message)
        verify(interactor, never()).saveSelectedFunctionId(ArgumentMatchers.anyInt())
    }
}