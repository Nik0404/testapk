package com.acroninspector.app.di.fragment.mediafiles

import android.content.Context
import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.data.repositories.FileRepositoryImpl
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.checklist.CheckListRepositoryModule
import com.acroninspector.app.di.repository.localdefect.LocalDefectRepositoryModule
import com.acroninspector.app.di.repository.mediafile.MediaFileRepositoryModule
import com.acroninspector.app.di.repository.route.RouteRepositoryModule
import com.acroninspector.app.di.repository.task.TaskRepositoryModule
import com.acroninspector.app.domain.interactors.mediafiles.MediaFilesInteractor
import com.acroninspector.app.domain.interactors.mediafiles.MediaFilesInteractorImpl
import com.acroninspector.app.domain.repositories.*
import com.acroninspector.app.presentation.adapter.mediafiles.MediaFilesAdapter
import com.acroninspector.app.presentation.dialog.MediaFilesDialog
import com.acroninspector.app.presentation.fragment.mediafiles.MediaFilesPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [
    MediaFileRepositoryModule::class,
    TaskRepositoryModule::class,
    RouteRepositoryModule::class,
    CheckListRepositoryModule::class,
    LocalDefectRepositoryModule::class
])
class MediaFilesModule(private val applicationContext: Context) : BaseModule {

    @PerScreen
    @Provides
    fun providePresenter(interactor: MediaFilesInteractor): MediaFilesPresenter {
        return MediaFilesPresenter(interactor)
    }

    @PerScreen
    @Provides
    fun provideMediaFilesInteractor(
            mediaFilesRepository: MediaFileRepository,
            taskRepository: TaskRepository,
            routeRepository: RouteRepository,
            checkListRepository: CheckListRepository,
            localDefectRepository: LocalDefectRepository,
            fileRepository: FileRepository,
            preferencesRepository: PreferencesRepository,
            schedulersProvider: SchedulersProvider
    ): MediaFilesInteractor {
        return MediaFilesInteractorImpl(
                mediaFilesRepository,
                taskRepository,
                routeRepository,
                checkListRepository,
                localDefectRepository,
                fileRepository,
                preferencesRepository,
                schedulersProvider
        )
    }

    @PerScreen
    @Provides
    fun provideFileRepository(): FileRepository {
        return FileRepositoryImpl(applicationContext)
    }

    @PerScreen
    @Provides
    fun provideAttachmentsAdapter(): MediaFilesAdapter {
        return MediaFilesAdapter()
    }

    @PerScreen
    @Provides
    fun provideAttachmentsDialog(): MediaFilesDialog {
        return MediaFilesDialog()
    }

}
