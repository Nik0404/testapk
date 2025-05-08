package com.acroninspector.app.di.fragment.defects.searchresult

import com.acroninspector.app.di.fragment.defects.DefectsBaseModule
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.domain.interactors.defectlog.DefectLogInteractor
import com.acroninspector.app.presentation.adapter.defects.DefectsAdapter
import com.acroninspector.app.presentation.fragment.defects.searchresult.DefectsSearchResultPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [DefectsBaseModule::class])
class DefectsSearchResultModule : BaseModule {

    @PerScreen
    @Provides
    fun providePresenter(interactor: DefectLogInteractor): DefectsSearchResultPresenter {
        return DefectsSearchResultPresenter(interactor)
    }

    @PerScreen
    @Provides
    fun provideAdapter(): DefectsAdapter {
        return DefectsAdapter()
    }
}