package com.acroninspector.app.di.fragment.questions

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.questions.QuestionsFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [QuestionsModule::class])
interface QuestionsComponent : BaseComponent<QuestionsFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<QuestionsComponent, QuestionsModule>
}