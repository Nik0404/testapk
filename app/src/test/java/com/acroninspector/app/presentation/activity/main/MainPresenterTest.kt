package com.acroninspector.app.presentation.activity.main

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.utils.NetworkErrorsParser
import com.acroninspector.app.data.util.support.AcronDataProcessingException
import com.acroninspector.app.domain.interactors.main.MainInteractor
import com.acroninspector.app.domain.interactors.sync.SyncInteractor
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
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
import retrofit2.Response

@RunWith(PowerMockRunner::class)
@PrepareForTest(NetworkErrorsParser::class)
class MainPresenterTest {

    private lateinit var presenter: MainPresenter

    private lateinit var networkErrorsParser: NetworkErrorsParser

    @Mock
    lateinit var mainInteractor: MainInteractor

    @Mock
    lateinit var syncInteractor: SyncInteractor

    @Mock
    lateinit var viewState: `MainView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        networkErrorsParser = PowerMockito.mock(NetworkErrorsParser::class.java)

        presenter = MainPresenter(mainInteractor, syncInteractor, networkErrorsParser)
        presenter.setViewState(viewState)
    }

    private fun getFakeUser(): com.acroninspector.app.domain.entity.local.database.User {
        return com.acroninspector.app.domain.entity.local.database.User(
                1, "login", "name", "surname",
                "thirdName", 0
        )
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

    @Test
    fun loadCurrentUser_Success() {
        val methodName = "loadCurrentUser"

        `when`(mainInteractor.getCurrentUser()).thenReturn(Single.just(getFakeUser()))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, mainInteractor)

        inOrder.verify(mainInteractor).getCurrentUser()
        inOrder.verify(viewState).setUser(getFakeUser())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyString())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun loadCurrentUser_Failed() {
        val methodName = "loadCurrentUser"

        `when`(mainInteractor.getCurrentUser()).thenReturn(Single.error(Exception()))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, mainInteractor)

        inOrder.verify(mainInteractor).getCurrentUser()
        inOrder.verify(viewState).showSnackbar(R.string.user_loading_error_message)
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyString())
        verify(viewState, never()).setUser(ArgumentMatchers.any())
    }

    @Test
    fun logout_Success() {
        `when`(syncInteractor.isDataUploadedToServer()).thenReturn(true)
        `when`(mainInteractor.logout()).thenReturn(Completable.complete())

        presenter.logout()

        val inOrder = inOrder(viewState, mainInteractor)

        inOrder.verify(viewState).closeDrawer()
        inOrder.verify(viewState).showProgressDialog(Constants.CLEAR_ALL_DATA)
        inOrder.verify(mainInteractor).logout()
        inOrder.verify(viewState).closeProgressDialog()
        inOrder.verify(viewState).openLoginActivity()

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyString())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).showSyncErrorDialog(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())
    }

    @Test
    fun logout_LocalFailed() {
        `when`(syncInteractor.isDataUploadedToServer()).thenReturn(true)
        `when`(mainInteractor.logout()).thenReturn(Completable.error(Exception()))

        presenter.logout()

        val inOrder = inOrder(viewState, mainInteractor)

        inOrder.verify(viewState).closeDrawer()
        inOrder.verify(mainInteractor).logout()
        inOrder.verify(viewState).openLoginActivity()

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyString())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).showSyncErrorDialog(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())
    }

    @Test
    fun logout_NetworkFailed() {
        `when`(syncInteractor.isDataUploadedToServer()).thenReturn(true)
        val error = getFakeErrorMessage()

        PowerMockito.`when`(networkErrorsParser.parseErrorMessage(error))
                .thenReturn(getFakeErrorMessage())

        `when`(mainInteractor.logout())
                .thenReturn(Completable.error(getFakeHttpException()))

        presenter.logout()

        val inOrder = inOrder(viewState, mainInteractor)

        inOrder.verify(viewState).closeDrawer()
        inOrder.verify(mainInteractor).logout()
        inOrder.verify(viewState).showSnackbar(getFakeErrorMessage())

        verify(viewState, never()).openLoginActivity()
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).showSyncErrorDialog(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())
    }

    @Test
    fun logout_dataIsNotUploaded() {
        `when`(syncInteractor.isDataUploadedToServer()).thenReturn(false)
        `when`(mainInteractor.logout()).thenReturn(Completable.error(Exception()))

        presenter.logout()

        verify(viewState).showSyncErrorDialog(R.string.data_is_not_uploaded_logout)

        verify(viewState, never()).closeDrawer()
        verify(mainInteractor, never()).logout()
        verify(viewState, never()).openLoginActivity()
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyString())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun loadNotificationsCount_Success() {
        val notificationsCount = 10
        val methodName = "loadNotificationsCount"

        `when`(mainInteractor.getNotificatoinsCount())
                .thenReturn(Flowable.just(notificationsCount))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, mainInteractor)

        inOrder.verify(mainInteractor).getNotificatoinsCount()
        inOrder.verify(viewState).setNotificationsCount(notificationsCount)
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyString())
    }

    @Test
    fun loadNotificationsCount_Failed() {
        val methodName = "loadNotificationsCount"

        `when`(mainInteractor.getNotificatoinsCount())
                .thenReturn(Flowable.error(Throwable()))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, mainInteractor)

        inOrder.verify(mainInteractor).getNotificatoinsCount()

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyString())
        verify(viewState, never()).setNotificationsCount(ArgumentMatchers.anyInt())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyString())
    }

    @Test
    fun loadDataFromServer_Success() {
        `when`(syncInteractor.isDataUploadedToServer())
                .thenReturn(true)

        `when`(syncInteractor.loadDataFromServer())
                .thenReturn(Flowable.just(1))

        presenter.syncDataWithServer(Constants.LOAD_DATA_FROM_SERVER)

        val inOrder = inOrder(viewState, syncInteractor)

        inOrder.verify(viewState).showProgressDialog(Constants.LOAD_DATA_FROM_SERVER)
        inOrder.verify(syncInteractor).loadDataFromServer()
        inOrder.verify(syncInteractor).dataLoadedFromSeverSuccessfully()
        inOrder.verify(viewState).closeProgressDialog()
        inOrder.verify(viewState).showSyncSuccessDialog(Constants.LOAD_DATA_FROM_SERVER)
        verify(viewState, never()).showSyncErrorDialog(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())
    }

    @Test
    fun loadDataFromServerFirstSync_Success() {
        `when`(syncInteractor.isDataUploadedToServer())
                .thenReturn(true)

        `when`(syncInteractor.registerFunctionAndLoadDataFromServer())
                .thenReturn(Flowable.just(1))

        presenter.syncDataWithServer(Constants.LOAD_DATA_FROM_SERVER, true)

        val inOrder = inOrder(viewState, syncInteractor)

        inOrder.verify(viewState).showProgressDialog(Constants.LOAD_DATA_FROM_SERVER)
        inOrder.verify(syncInteractor).registerFunctionAndLoadDataFromServer()
        inOrder.verify(syncInteractor).dataLoadedFromSeverSuccessfully()
        inOrder.verify(viewState).closeProgressDialog()
        inOrder.verify(viewState).showSyncSuccessDialog(Constants.LOAD_DATA_FROM_SERVER)
        verify(viewState, never()).showSyncErrorDialog(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())
    }

    @Test
    fun uploadDataToServer_Success() {
        `when`(syncInteractor.uploadDataToServer())
                .thenReturn(Flowable.just(1))

        presenter.syncDataWithServer(Constants.UPLOAD_DATA_TO_SERVER)

        val inOrder = inOrder(viewState, syncInteractor)

        inOrder.verify(viewState).showProgressDialog(Constants.UPLOAD_DATA_TO_SERVER)
        inOrder.verify(syncInteractor).uploadDataToServer()
        inOrder.verify(syncInteractor).dataUploadedToSeverSuccessfully()
        inOrder.verify(viewState).closeProgressDialog()
        inOrder.verify(viewState).showSyncSuccessDialog(Constants.UPLOAD_DATA_TO_SERVER)
        verify(viewState, never()).showSyncErrorDialog(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())
    }

    @Test
    fun loadDataFromServer_Failed() {
        val functionName = "ME_MOEX_EXEMPLAR_TASK"
        val entityId = R.string.tasks_for_error_message

        `when`(syncInteractor.isDataUploadedToServer())
                .thenReturn(true)

        `when`(syncInteractor.loadDataFromServer())
                .thenReturn(Flowable.error(AcronDataProcessingException(functionName, Throwable())))

        presenter.syncDataWithServer(Constants.LOAD_DATA_FROM_SERVER)

        val inOrder = inOrder(viewState, syncInteractor)

        inOrder.verify(viewState).showProgressDialog(Constants.LOAD_DATA_FROM_SERVER)
        inOrder.verify(syncInteractor).loadDataFromServer()
        inOrder.verify(viewState).closeProgressDialog()
        inOrder.verify(viewState).showSyncErrorDialog(R.string.get_data_error, entityId)
        verify(viewState, never()).showSyncSuccessDialog(Constants.LOAD_DATA_FROM_SERVER)
    }

    @Test
    fun uploadDataToServer_Failed() {
        val functionName = "ME_MOEX_EXEMPLAR_TASK"
        val entityId = R.string.tasks_for_error_message

        `when`(syncInteractor.uploadDataToServer())
                .thenReturn(Flowable.error(AcronDataProcessingException(functionName, Throwable())))

        presenter.syncDataWithServer(Constants.UPLOAD_DATA_TO_SERVER)

        val inOrder = inOrder(viewState, syncInteractor)

        inOrder.verify(viewState).showProgressDialog(Constants.UPLOAD_DATA_TO_SERVER)
        inOrder.verify(syncInteractor).uploadDataToServer()
        inOrder.verify(viewState).closeProgressDialog()
        inOrder.verify(viewState).showSyncErrorDialog(R.string.send_data_error, entityId)
        verify(viewState, never()).showSyncSuccessDialog(Constants.UPLOAD_DATA_TO_SERVER)
    }
}
