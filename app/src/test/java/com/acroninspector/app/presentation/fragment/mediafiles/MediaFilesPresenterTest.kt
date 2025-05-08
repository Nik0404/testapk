package com.acroninspector.app.presentation.fragment.mediafiles

import android.os.Parcel
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.Constants.ENTITY_CHECK_LIST
import com.acroninspector.app.domain.entity.local.display.DisplayMediaFile
import com.acroninspector.app.domain.interactors.mediafiles.MediaFilesInteractor
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox

@RunWith(PowerMockRunner::class)
@PrepareForTest(Parcel::class)
class MediaFilesPresenterTest {

    private lateinit var presenter: MediaFilesPresenter

    @Mock
    lateinit var interactor: MediaFilesInteractor

    @Mock
    lateinit var viewState: `MediaFilesView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = MediaFilesPresenter(interactor)
        presenter.setViewState(viewState)
    }

    private fun getFakeId(): Int {
        return 1
    }

    private fun getFakeMediaFile(mediaType: Int): DisplayMediaFile {
        return DisplayMediaFile(getFakeId(), mediaType, "filePath", "uri")
    }

    private fun getFakeMediaFiles(size: Int): List<DisplayMediaFile> {
        val mediaFiles = mutableListOf<DisplayMediaFile>()
        for (i in 0 until size) {
            val mediaFile = getFakeMediaFile(Constants.MEDIA_TYPE_PHOTO)
            mediaFiles.add(mediaFile)
        }

        return mediaFiles
    }

    @Test
    fun loadMediaFiles_Success() {
        val methodName = "loadMediaFiles"
        val entityId = 1
        val entityType = ENTITY_CHECK_LIST

        val fakeMediaFiles = getFakeMediaFiles(5)

        `when`(interactor.getMediaFilesByEntityId(ENTITY_CHECK_LIST, entityId))
                .thenReturn(Flowable.just(fakeMediaFiles))

        presenter.entityId = entityId
        presenter.entityType = entityType
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getMediaFilesByEntityId(ENTITY_CHECK_LIST, entityId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateMediaFiles(fakeMediaFiles)
        inOrder.verify(viewState).hideEmptyState()
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyString())
        verify(viewState, never()).showEmptyState()
    }

    @Test
    fun loadMediaFiles_EmptyData() {
        val methodName = "loadMediaFiles"
        val entityId = 1
        val entityType = ENTITY_CHECK_LIST

        val fakeMediaFiles = emptyList<DisplayMediaFile>()

        `when`(interactor.getMediaFilesByEntityId(ENTITY_CHECK_LIST, entityId))
                .thenReturn(Flowable.just(fakeMediaFiles))

        presenter.entityId = entityId
        presenter.entityType = entityType
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getMediaFilesByEntityId(ENTITY_CHECK_LIST, entityId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateMediaFiles(fakeMediaFiles)
        inOrder.verify(viewState).showEmptyState()
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyString())
        verify(viewState, never()).hideEmptyState()
    }

    @Test
    fun loadMediaFiles_Failed() {
        val methodName = "loadMediaFiles"
        val entityId = 1
        val entityType = ENTITY_CHECK_LIST

        `when`(interactor.getMediaFilesByEntityId(ENTITY_CHECK_LIST, entityId))
                .thenReturn(Flowable.error(Throwable()))

        presenter.entityId = entityId
        presenter.entityType = entityType
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getMediaFilesByEntityId(ENTITY_CHECK_LIST, entityId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showEmptyState()
        verify(viewState, never()).hideEmptyState()
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyString())
        verify(viewState, never()).updateMediaFiles(ArgumentMatchers.any())
    }

    @Test
    fun onClickPhotoMediaFile() {
        val fakeMediaFile = getFakeMediaFile(Constants.MEDIA_TYPE_PHOTO)

        Whitebox.setInternalState(presenter, "mediaFiles", listOf(fakeMediaFile))
        presenter.onClickMediaFile(0)

        val inOrder = inOrder(viewState)

        inOrder.verify(viewState).openImageViewer(fakeMediaFile.filePath)
        verify(viewState, never()).openPlayer(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyString())
    }

    @Test
    fun onClickVideoMediaFile() {
        val fakeMediaFile = getFakeMediaFile(Constants.MEDIA_TYPE_VIDEO)

        Whitebox.setInternalState(presenter, "mediaFiles", listOf(fakeMediaFile))
        presenter.onClickMediaFile(0)

        val inOrder = inOrder(viewState)

        inOrder.verify(viewState).openPlayer(fakeMediaFile.uri, fakeMediaFile.mediaType)
        verify(viewState, never()).openImageViewer(ArgumentMatchers.anyString())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyString())
    }

    @Test
    fun onClickAudioMediaFile() {
        val fakeMediaFile = getFakeMediaFile(Constants.MEDIA_TYPE_AUDIO)

        Whitebox.setInternalState(presenter, "mediaFiles", listOf(fakeMediaFile))
        presenter.onClickMediaFile(0)

        val inOrder = inOrder(viewState)

        inOrder.verify(viewState).openPlayer(fakeMediaFile.uri, fakeMediaFile.mediaType)
        verify(viewState, never()).openImageViewer(ArgumentMatchers.anyString())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyString())
    }

    @Test
    fun onClickDeleteFile_Success() {
        val fakeMediaFiles = getFakeMediaFiles(3)
        val position = 2

        val entityId = 1
        presenter.entityId = entityId

        val entityType = ENTITY_CHECK_LIST
        presenter.entityType = entityType

        Whitebox.setInternalState(presenter, "mediaFiles", fakeMediaFiles)

        `when`(interactor.deleteMediaFile(fakeMediaFiles[position].id, entityId, entityType))
                .thenReturn(Completable.complete())

        presenter.onClickDeleteFile(position)

        verify(interactor).deleteMediaFile(
                fakeMediaFiles[position].id,
                entityId, entityType
        )
    }

    @Test
    fun onClickDeleteFile_Failed() {
        val fakeMediaFiles = getFakeMediaFiles(3)
        val position = 2

        val entityId = 1
        presenter.entityId = entityId

        val entityType = ENTITY_CHECK_LIST
        presenter.entityType = entityType

        Whitebox.setInternalState(presenter, "mediaFiles", fakeMediaFiles)

        `when`(interactor.deleteMediaFile(fakeMediaFiles[position].id, entityId, entityType))
                .thenReturn(Completable.error(Throwable()))

        presenter.onClickDeleteFile(position)

        verify(interactor).deleteMediaFile(
                fakeMediaFiles[position].id,
                entityId, entityType
        )
        verify(viewState).showSnackbar(R.string.error_message)
    }
}