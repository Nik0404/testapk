package com.acroninspector.app.presentation.fragment.mediafiles

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_TYPE
import com.acroninspector.app.common.constants.Constants.MEDIA_TYPE_AUDIO
import com.acroninspector.app.common.constants.Constants.MEDIA_TYPE_PHOTO
import com.acroninspector.app.common.constants.Constants.MEDIA_TYPE_VIDEO
import com.acroninspector.app.domain.entity.local.database.MediaFile
import com.acroninspector.app.domain.entity.local.display.DisplayMediaFile
import com.acroninspector.app.domain.interactors.mediafiles.MediaFilesInteractor
import com.acroninspector.app.presentation.mvp.BasePresenter
import com.arellomobile.mvp.InjectViewState
import io.reactivex.Flowable
import timber.log.Timber


@InjectViewState
class MediaFilesPresenter(
        private val interactor: MediaFilesInteractor
) : BasePresenter<MediaFilesView>() {

    /**
     * Flag that indicates whether this fragment opens from creating defect or not
     */
    var isCreatingDefect = false

    var entityId = DEFAULT_INVALID_ID
    var entityType = DEFAULT_INVALID_TYPE

    private var mediaFiles: List<DisplayMediaFile> = ArrayList()

    private lateinit var filePath: String
    private lateinit var fileUri: Uri

    private val showErrorConsumer: ((t: Throwable) -> Unit) = {
        viewState.showSnackbar(R.string.error_message)
        Timber.e(it)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadMediaFiles()
    }

    private fun loadMediaFiles() {
        if (entityId != DEFAULT_INVALID_ID && entityType != DEFAULT_INVALID_TYPE) {
            viewState.showProgress()
            subscriptions.add(handleLoadingMediaFiles()
                    .subscribe({
                        viewState.hideProgress()
                        viewState.updateMediaFiles(it)

                        handleEmptyState(it.isEmpty())
                        mediaFiles = it
                    }, {
                        viewState.hideProgress()
                        viewState.showSnackbar(R.string.error_message)
                        handleEmptyState(true)

                        Timber.e(it)
                    })
            )
        } else viewState.showSnackbar(R.string.error_message)
    }

    private fun handleLoadingMediaFiles(): Flowable<List<DisplayMediaFile>> {
        return if (isCreatingDefect) {
            interactor.getMediaFilesByCreatedDefect(entityId)
        } else interactor.getMediaFilesByEntityId(entityType, entityId)
    }

    fun insertAudioFile(uri: Uri) {
        subscriptions.add(interactor
                .insertAudioFile(MEDIA_TYPE_AUDIO, uri, entityId, entityType)
                .subscribe({}, showErrorConsumer)
        )
    }

    fun insertMediaFile(requestCode: Int) {
        val mediaFile = MediaFile(requestCode, filePath, fileUri.toString())
        subscriptions.add(interactor
                .insertMediaFile(mediaFile, entityId, entityType)
                .subscribe({}, showErrorConsumer)
        )
    }

    fun onClickMediaFile(position: Int) {
        val mediaFile = mediaFiles[position]
        when {
            mediaFile.mediaType == MEDIA_TYPE_PHOTO -> viewState.openImageViewer(mediaFile.filePath)
            mediaFile.mediaType == MEDIA_TYPE_VIDEO || mediaFile.mediaType == MEDIA_TYPE_AUDIO ->
                viewState.openPlayer(mediaFile.uri, mediaFile.mediaType)
        }
    }

    fun onClickDeleteFile(position: Int) {
        val mediaFileId = mediaFiles[position].id

        subscriptions.add(interactor
                .deleteMediaFile(mediaFileId, entityId, entityType)
                .subscribe({}, showErrorConsumer)
        )
    }

    fun onClickAddPhoto() {
        subscriptions.add(interactor
                .createFile(MEDIA_TYPE_PHOTO)
                .subscribe({
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            .putExtra(MediaStore.EXTRA_OUTPUT, it.uri)
                    filePath = it.filePath
                    fileUri = it.uri

                    viewState.openDefaultMediaRecorder(intent, MEDIA_TYPE_PHOTO)
                }, showErrorConsumer)
        )
    }

    fun onClickAddVideo() {
        subscriptions.add(interactor
                .createFile(MEDIA_TYPE_VIDEO)
                .subscribe({
                    filePath = it.filePath
                    fileUri = it.uri
                    viewState.openDefaultVideoRecorder(it)
                }, showErrorConsumer)
        )
    }

    fun onClickAddAudio() {
        val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
        viewState.openDefaultMediaRecorder(intent, MEDIA_TYPE_AUDIO)
    }

    private fun handleEmptyState(isListEmpty: Boolean) {
        if (isListEmpty) {
            viewState.showEmptyState()
        } else viewState.hideEmptyState()
    }
}