package com.acroninspector.app.di.fragment.search

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.search.SearchFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [SearchModule::class])
interface SearchComponent : BaseComponent<SearchFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<SearchComponent, SearchModule>
}