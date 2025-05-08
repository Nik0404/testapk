package com.acroninspector.app.di.fragment.edittask

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.executor.ExecutorRepositoryModule
import com.acroninspector.app.di.repository.task.TaskRepositoryModule
import com.acroninspector.app.domain.interactors.edittask.EditTaskInteractor
import com.acroninspector.app.domain.interactors.edittask.EditTaskInteractorImpl
import com.acroninspector.app.domain.interactors.executor.ExecutorInteractor
import com.acroninspector.app.domain.interactors.executor.ExecutorInteractorImpl
import com.acroninspector.app.domain.repositories.ExecutorRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.TaskRepository
import com.acroninspector.app.presentation.dialog.ExecutorsDialog
import com.acroninspector.app.presentation.fragment.edittask.EditTaskPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [
    TaskRepositoryModule::class,
    ExecutorRepositoryModule::class])
class EditTaskModule : BaseModule {

    @PerScreen
    @Provides
    fun providePresenter(
            editTaskInteractor: EditTaskInteractor
    ): EditTaskPresenter {
        return EditTaskPresenter(editTaskInteractor)
    }

    @PerScreen
    @Provides
    fun provideInteractor(
            taskRepository: TaskRepository,
            preferencesRepository: PreferencesRepository,
            schedulersProvider: SchedulersProvider
    ): EditTaskInteractor {
        return EditTaskInteractorImpl(taskRepository, preferencesRepository, schedulersProvider)
    }

    @Provides
    fun provideExecutorsDialog(interactor: ExecutorInteractor): ExecutorsDialog {
        return ExecutorsDialog(interactor)
    }

    @PerScreen
    @Provides
    fun provideExecutorInteractor(
            executorRepository: ExecutorRepository,
            schedulersProvider: SchedulersProvider
    ): ExecutorInteractor {
        return ExecutorInteractorImpl(executorRepository, schedulersProvider)
    }
}