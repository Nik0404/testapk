package com.acroninspector.app.presentation.fragment.tasks.completed

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.acroninspector.app.App
import com.acroninspector.app.R
import com.acroninspector.app.databinding.FragmentTasksBinding
import com.acroninspector.app.presentation.adapter.tasks.TasksAdapter
import com.acroninspector.app.presentation.fragment.tasks.TasksFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import javax.inject.Inject

class CompletedTasksFragment : TasksFragment(), CompletedTasksView {

    private lateinit var binding: FragmentTasksBinding

    @Inject
    lateinit var tasksAdapter: TasksAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: CompletedTasksPresenter

    @ProvidePresenter
    fun providePresenter(): CompletedTasksPresenter = presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBindingView(R.layout.fragment_tasks, inflater, container)
        binding.swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(context!!, R.color.colorBlue),
                ContextCompat.getColor(context!!, R.color.colorPurple),
                ContextCompat.getColor(context!!, R.color.colorGreen)
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tasksAdapter.setOnItemClickListener { presenter.onTaskClicked(it) }
        getSwipeRefreshLayout().setOnRefreshListener { presenter.refreshTasks() }
    }

    override fun getRootView(): View = binding.root

    override fun getSwipeRefreshLayout(): SwipeRefreshLayout = binding.swipeRefreshLayout

    override fun getRecyclerView(): RecyclerView = binding.recyclerTasks

    override fun getEmptyStateView(): View = binding.emptyStateView

    override fun getTasksRecyclerAdapter(): TasksAdapter = tasksAdapter

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}
