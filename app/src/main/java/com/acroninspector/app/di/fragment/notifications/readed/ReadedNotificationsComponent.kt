package com.acroninspector.app.di.fragment.notifications.readed

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.notifications.readed.ReadedNotificationsFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [ReadedNotificationsModule::class])
interface ReadedNotificationsComponent : BaseComponent<ReadedNotificationsFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<ReadedNotificationsComponent, ReadedNotificationsModule>
}