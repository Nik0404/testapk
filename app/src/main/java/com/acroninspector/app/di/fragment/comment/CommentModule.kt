package com.acroninspector.app.di.fragment.comment

import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.comment.CommentPresenter
import dagger.Module
import dagger.Provides

@Module
class CommentModule : BaseModule {

    @PerScreen
    @Provides
    fun providePresenter(): CommentPresenter {
        return CommentPresenter()
    }
}