package com.acroninspector.app.di.fragment.comment

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.comment.CommentFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [CommentModule::class])
interface CommentComponent : BaseComponent<CommentFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<CommentComponent, CommentModule>
}