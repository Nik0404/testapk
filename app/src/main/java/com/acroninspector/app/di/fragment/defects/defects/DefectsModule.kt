package com.acroninspector.app.di.fragment.defects.defects

import com.acroninspector.app.di.fragment.defects.DefectsBaseModule
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.domain.interactors.defectlog.DefectLogInteractor
import com.acroninspector.app.presentation.adapter.defects.DefectsAdapter
import com.acroninspector.app.presentation.fragment.defects.defectlogs.DefectLogsPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [DefectsBaseModule::class])
class DefectsModule : BaseModule {

    @PerScreen
    @Provides
    fun providePresenter(interactor: DefectLogInteractor): DefectLogsPresenter {
        return DefectLogsPresenter(interactor)
    }

    @PerScreen
    @Provides
    fun provideAdapter(): DefectsAdapter {
        return DefectsAdapter()
    }
}