package com.acroninspector.app.presentation.fragment.taskcomment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.acroninspector.app.App
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.Constants.KEY_TASK_OBJECT
import com.acroninspector.app.common.constants.DatabaseConstants
import com.acroninspector.app.databinding.FragmentTaskCommentBinding
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.presentation.activity.main.MainView
import com.acroninspector.app.presentation.mvp.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import javax.inject.Inject


class TaskCommentFragment : BaseFragment(), TaskCommentView {

    private lateinit var binding: FragmentTaskCommentBinding

    private lateinit var navController: NavController

    @Inject
    @InjectPresenter
    lateinit var presenter: TaskCommentPresenter

    @ProvidePresenter
    fun providePresenter(): TaskCommentPresenter = presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBindingView(R.layout.fragment_task_comment, inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        presenter.taskId = arguments?.getInt(Constants.KEY_TASK_ID, Constants.DEFAULT_INVALID_ID)!!

        val taskStatus = arguments?.getInt(Constants.KEY_TASK_STATUS, Constants.DEFAULT_INVALID_STATUS)!!
        binding.status = taskStatus
        presenter.taskStatus = taskStatus
        prepareButtons(taskStatus)

        if (taskStatus == DatabaseConstants.TASK_STATUS_IN_PROGRESS) {
            binding.etComment.requestFocus()
            showKeyboard()
        } else {
            binding.etComment.isEnabled = false
        }

        binding.btnCancel.setOnClickListener { closeFragment() }
        binding.btnApply.setOnClickListener {
            presenter.onApplyClicked(binding.etComment.text.toString())
        }

        val task = arguments?.getParcelable<DisplayTask>(KEY_TASK_OBJECT)!!
        setTask(task)
    }

    private fun prepareButtons(taskStatus: Int) {
        if (taskStatus == DatabaseConstants.TASK_STATUS_COMPLETED) {
            binding.btnApply.visibility = View.INVISIBLE
            binding.btnCancel.visibility = View.INVISIBLE
        }
    }

    override fun setTask(task: DisplayTask) {
        binding.task = task
        binding.etComment.setSelection(binding.etComment.text?.length!!)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(binding.root)
    }

    override fun showSnackbar(resourceId: Int) {
        (activity as? MainView)?.showSnackbar(resourceId)
    }

    override fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    override fun showKeyboard() {
        Handler().postDelayed({
            val inputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }, 250)
    }

    override fun hideKeyboard() {
        val inputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    override fun closeFragment() {
        hideKeyboard()
        navController.popBackStack()
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}