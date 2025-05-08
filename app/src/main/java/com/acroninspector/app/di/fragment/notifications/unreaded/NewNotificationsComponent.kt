package com.acroninspector.app.di.fragment.notifications.unreaded

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.notifications.unreaded.NewNotificationsFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [NewNotificationsModule::class])
interface NewNotificationsComponent : BaseComponent<NewNotificationsFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<NewNotificationsComponent, NewNotificationsModule>
}