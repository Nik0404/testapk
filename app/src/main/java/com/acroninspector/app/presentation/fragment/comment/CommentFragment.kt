package com.acroninspector.app.presentation.fragment.comment

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
import com.acroninspector.app.common.constants.Constants.KEY_COMMENT
import com.acroninspector.app.databinding.FragmentCommentBinding
import com.acroninspector.app.presentation.activity.main.MainActivity
import com.acroninspector.app.presentation.activity.main.MainView
import com.acroninspector.app.presentation.fragment.comment.listener.PassCommentListener
import com.acroninspector.app.presentation.mvp.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import javax.inject.Inject


class CommentFragment : BaseFragment(), CommentView, PassCommentListener {

    private lateinit var binding: FragmentCommentBinding

    private lateinit var navController: NavController

    @Inject
    @InjectPresenter
    lateinit var presenter: CommentPresenter

    @ProvidePresenter
    fun providePresenter(): CommentPresenter = presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBindingView(R.layout.fragment_comment, inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val comment = arguments?.getString(KEY_COMMENT)
        if (!comment.isNullOrEmpty()) {
            binding.etComment.setText(comment)
        }

        binding.btnCancel.setOnClickListener { closeFragment() }
        binding.btnApply.setOnClickListener {
            presenter.onApplyClicked(binding.etComment.text.toString())
        }

        val enableEditing = arguments?.getBoolean(Constants.KEY_ENABLED_EDITING, false)!!
        binding.etComment.isEnabled = enableEditing

        if (enableEditing) {
            binding.etComment.setSelection(binding.etComment.text?.length!!)
            binding.etComment.requestFocus()
            showKeyboard()
        } else {
            binding.btnCancel.visibility = View.INVISIBLE
            binding.btnApply.visibility = View.INVISIBLE
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(binding.root)
    }

    override fun passComment(comment: String) {
        (activity as? MainActivity)?.passDefectComment(comment)
        (activity as? MainActivity)?.releaseDefectCommentListener()
        closeFragment()
    }

    override fun showSnackbar(resourceId: Int) {
        (activity as? MainView)?.showSnackbar(resourceId)
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