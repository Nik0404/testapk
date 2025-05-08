package com.acroninspector.app.di.fragment.search

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.search.SearchRepositoryModule
import com.acroninspector.app.domain.interactors.search.SearchInteractor
import com.acroninspector.app.domain.interactors.search.SearchInteractorImpl
import com.acroninspector.app.domain.repositories.SearchRepository
import com.acroninspector.app.presentation.adapter.search.SearchAdapter
import com.acroninspector.app.presentation.fragment.search.SearchPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [SearchRepositoryModule::class])
class SearchModule : BaseModule {

    @PerScreen
    @Provides
    fun providePresenter(interactor: SearchInteractor): SearchPresenter {
        return SearchPresenter(interactor)
    }

    @PerScreen
    @Provides
    fun provideInteractor(
            searchRepository: SearchRepository,
            schedulersProvider: SchedulersProvider
    ): SearchInteractor {
        return SearchInteractorImpl(searchRepository, schedulersProvider)
    }

    @PerScreen
    @Provides
    fun provideSearchAdapter(): SearchAdapter {
        return SearchAdapter()
    }
}