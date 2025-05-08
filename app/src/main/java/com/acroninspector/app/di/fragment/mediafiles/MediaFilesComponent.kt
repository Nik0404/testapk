package com.acroninspector.app.di.fragment.mediafiles

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.mediafiles.MediaFilesFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [MediaFilesModule::class])
interface MediaFilesComponent : BaseComponent<MediaFilesFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<MediaFilesComponent, MediaFilesModule>
}