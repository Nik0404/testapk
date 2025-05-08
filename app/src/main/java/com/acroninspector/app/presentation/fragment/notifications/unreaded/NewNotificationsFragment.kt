package com.acroninspector.app.presentation.fragment.notifications.unreaded

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.App
import com.acroninspector.app.R
import com.acroninspector.app.databinding.FragmentNotificationsBinding
import com.acroninspector.app.presentation.adapter.notifications.NotificationsAdapter
import com.acroninspector.app.presentation.fragment.notifications.NotificationsFragment
import com.acroninspector.app.presentation.fragment.notifications.NotificationsPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import javax.inject.Inject

class NewNotificationsFragment : NotificationsFragment(), NewNotificationsView {

    private lateinit var binding: FragmentNotificationsBinding

    @Inject
    lateinit var notificationsAdapter: NotificationsAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: NewNotificationsPresenter

    @ProvidePresenter
    fun providePresenter(): NewNotificationsPresenter = presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBindingView(R.layout.fragment_notifications, inflater, container)
        return binding.root
    }

    override fun getRootView(): View = binding.root

    override fun getProgressBar(): View = binding.progressBar

    override fun getRecyclerView(): RecyclerView = binding.recyclerNotifications

    override fun getEmptyStateView(): View = binding.emptyStateView

    override fun getNotificationsRecyclerAdapter(): NotificationsAdapter = notificationsAdapter

    override fun getBasePresenter(): NotificationsPresenter<NewNotificationsView> = presenter

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}