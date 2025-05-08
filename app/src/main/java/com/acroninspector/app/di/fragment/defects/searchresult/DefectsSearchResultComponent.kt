package com.acroninspector.app.di.fragment.defects.searchresult

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.defects.searchresult.DefectsSearchResultFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [DefectsSearchResultModule::class])
interface DefectsSearchResultComponent : BaseComponent<DefectsSearchResultFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<DefectsSearchResultComponent, DefectsSearchResultModule>
}