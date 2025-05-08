package com.acroninspector.app.presentation.fragment.mediafiles

import android.content.Intent
import com.acroninspector.app.domain.entity.local.other.AcronFile
import com.acroninspector.app.domain.entity.local.display.DisplayMediaFile
import com.acroninspector.app.presentation.mvp.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface MediaFilesView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openDefaultMediaRecorder(intent: Intent, requestCode: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openImageViewer(filePath: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openPlayer(uri: String, mediaType: Int)

    fun updateMediaFiles(mediaFiles: List<DisplayMediaFile>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMediaFilesDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSnackbar(resourceId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSnackbar(message: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showEmptyState()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideEmptyState()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openDefaultVideoRecorder(file: AcronFile)
}