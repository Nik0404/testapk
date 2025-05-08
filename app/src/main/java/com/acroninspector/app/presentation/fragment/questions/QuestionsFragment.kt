package com.acroninspector.app.presentation.fragment.questions

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.acroninspector.app.App
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.Constants.KEY_TASK_STATUS
import com.acroninspector.app.databinding.FragmentQuestionsBinding
import com.acroninspector.app.domain.entity.local.display.DisplayAnswer
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.domain.entity.local.display.DisplayRoute
import com.acroninspector.app.presentation.activity.main.MainActivity
import com.acroninspector.app.presentation.activity.main.MainView
import com.acroninspector.app.presentation.adapter.question.QuestionItemAnimator
import com.acroninspector.app.presentation.adapter.question.QuestionsAdapter
import com.acroninspector.app.presentation.adapter.question.listener.OnClickQuestionListener
import com.acroninspector.app.presentation.custom.listener.NetworkStatusHandler
import com.acroninspector.app.presentation.custom.listener.NfcStatusHandler
import com.acroninspector.app.presentation.custom.listener.ScanNfcListener
import com.acroninspector.app.presentation.dialog.EquipmentCardDialogByPass
import com.acroninspector.app.presentation.dialog.ErrorDialog
import com.acroninspector.app.presentation.dialog.SelectAnswerDialog
import com.acroninspector.app.presentation.fragment.comment.listener.PassCommentListener
import com.acroninspector.app.presentation.mvp.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.sdk27.coroutines.onClick
import javax.inject.Inject
import javax.inject.Provider

class QuestionsFragment : BaseFragment(), QuestionsView, NetworkStatusHandler,
        NfcStatusHandler, PassCommentListener, ScanNfcListener {

    private lateinit var binding: FragmentQuestionsBinding

    private lateinit var navController: NavController

    private lateinit var adapter: QuestionsAdapter

    private var taskStatus: Int = 0

    @Inject
    lateinit var byPassEquipmentDialog: Provider<EquipmentCardDialogByPass>

    @Inject
    @InjectPresenter
    lateinit var presenter: QuestionsPresenter

    @ProvidePresenter
    fun providePresenter(): QuestionsPresenter = presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskStatus = arguments?.getInt(KEY_TASK_STATUS)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = getDataBindingView(R.layout.fragment_questions, inflater, container)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)

        (activity as? MainActivity)?.setNfcStatusHandler(this)
        (activity as? MainActivity)?.setNetworkStatusHandler(this)

        binding.btnBack.onClick { closeFragment() }
        binding.btnAccount.onClick { (activity as? MainView)?.showUserCardDialog() }
        binding.btnNfc.onClick { (activity as? MainView)?.openNfcSettingsActivity() }

        binding.btnRouteAttachments.onClick { presenter.onRouteAttachmentsClicked() }
        binding.btnRouteDefects.onClick { presenter.onRouteDefectsClicked() }

        presenter.taskId = arguments?.getInt(Constants.KEY_TASK_ID, Constants.DEFAULT_INVALID_ID)!!

        prepareTaskStatus()
        prepareHeader()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews(view: View) {
        binding.recyclerQuestions.layoutManager = LinearLayoutManager(context)
        binding.recyclerQuestions.itemAnimator = QuestionItemAnimator()

        adapter = QuestionsAdapter(taskStatus, object : OnClickQuestionListener {
            override fun onClickMenu() = presenter.onClickMenu()

            override fun onClickAttachments(position: Int) = presenter.onClickAttachments(position)

            override fun onClickComment(position: Int) = presenter.onClickComment(position)

            override fun onClickEditDefect(position: Int) = presenter.onClickEditDefect(position)

            override fun onClickSelectAnswer(position: Int, answerSelected: (String) -> Unit) {
                presenter.onClickSelectAnswer(position, answerSelected)
            }

            override fun updateAnswer(position: Int, answer: String) {
                presenter.updateAnswer(position, answer)
            }
        })
        binding.recyclerQuestions.adapter = adapter

        binding.recyclerQuestions.setOnTouchListener { _, _ ->
            hideKeyboard()
            view.clearFocus()
            false
        }
        binding.btnDefects.setOnClickListener {
            presenter.showEquipmentDeffect()
        }
    }

    private fun prepareTaskStatus() {
        presenter.taskStatus = taskStatus
        binding.status = taskStatus
    }

    private fun prepareHeader() {
        val route = arguments?.getParcelable<DisplayRoute>(Constants.KEY_ROUTE_OBJECT)!!
        presenter.routeId = route.id
        presenter.equipmentId = route.equipmentId
        setRoute(route)
    }

    override fun passComment(comment: String) {
        presenter.onCommentChanged(comment)
    }

    override fun setRoute(route: DisplayRoute) {
        binding.route = route
    }

    override fun updateQuestions(questions: List<DisplayQuestion>) {
        adapter.items = questions
    }

    override fun onNfcScanned(nfcCode: String) {
        presenter.onNfcScanned(nfcCode)
    }

    override fun openQuestionsFragment(taskId: Int, taskStatus: Int, route: DisplayRoute) {
        val args = Bundle()
        args.apply {
            putInt(Constants.KEY_TASK_ID, taskId)
            putInt(KEY_TASK_STATUS, taskStatus)
            putParcelable(Constants.KEY_ROUTE_OBJECT, route)
        }

        navController.navigate(R.id.action_questionsFragment_self, args)
    }

    override fun openRegisterDefectFragment(defectLogId: Int, checkListId: Int, equipmentId: Int, taskId: Int) {
        val args = Bundle()
        args.apply {
            putInt(Constants.KEY_ENTITY_TYPE, Constants.ENTITY_CHECK_LIST)
            putInt(Constants.KEY_DEFECT_LOG_ID, defectLogId)
            putInt(Constants.KEY_CHECK_LIST_ID, checkListId)
            putInt(Constants.KEY_EQUIPMENT_ID, equipmentId)
            putInt(Constants.KEY_TASK_ID, taskId)
        }

        navController.navigate(R.id.action_questionsFragment_to_registerDefectFragment, args)
    }

    override fun openRegisterDefectFragment(equipmentId: Int) {
        val args = Bundle()
        args.putInt(Constants.KEY_EQUIPMENT_ID, equipmentId)

        Navigation.findNavController(binding.root)
            .navigate(R.id.action_questionsFragment_to_registerDefectFragment, args)
    }

    // shw local defect list
    override fun openDefectsFragment(entityId: Int, entityType: Int, couldEdit: Boolean) {
        val args = Bundle()
        args.putInt(Constants.KEY_ENTITY_ID, entityId)
        args.putInt(Constants.KEY_ENTITY_TYPE, entityType)
//        args.putBoolean(Constants.KEY_COULD_EDIT, couldEdit)

        navController.navigate(R.id.action_questionsFragment_to_defectsSearchResultFragment, args)
    }

    override fun openMediaFilesFragment(entityId: Int, entityType: Int, enableEditing: Boolean) {
        val args = Bundle()
        args.putInt(Constants.KEY_ENTITY_ID, entityId)
        args.putInt(Constants.KEY_ENTITY_TYPE, entityType)
        args.putBoolean(Constants.KEY_ENABLED_EDITING, enableEditing)

        navController.navigate(R.id.action_questionsFragment_to_mediaFilesFragment, args)
    }

    override fun openCommentFragment(comment: String, enableEditing: Boolean) {
        (activity as? MainActivity)?.setPassDefectCommentListener(this)

        val args = Bundle()
        args.putString(Constants.KEY_COMMENT, comment)
        args.putBoolean(Constants.KEY_ENABLED_EDITING, enableEditing)

        navController.navigate(R.id.action_questionsFragment_to_commentFragment, args)
    }

    override fun openDefectDetailsFragment(defectLog: DisplayDefectLog) {
        val args = Bundle()
        args.putParcelable(Constants.KEY_DEFECT_OBJECT, defectLog)

        navController.navigate(R.id.action_questionsFragment_to_defectDetailsFragment, args)
    }

    override fun showSelectAnswerDialog(answers: List<DisplayAnswer>, position: Int,
                                        onAnswerSelected: (String) -> Unit) {
        val dialog = childFragmentManager.findFragmentByTag(SelectAnswerDialog.TAG)
        if (dialog == null) {
            SelectAnswerDialog(answers, onAnswerSelected)
                    .show(childFragmentManager, SelectAnswerDialog.TAG)
            childFragmentManager.executePendingTransactions()
        }
    }

    override fun showErrorDialog(title: Int, message: Int, text: String) {
        val dialog = childFragmentManager.findFragmentByTag(ErrorDialog.TAG)
        if (dialog == null) {
            ErrorDialog(getString(title), getString(message, text))
                    .show(childFragmentManager, ErrorDialog.TAG)
            childFragmentManager.executePendingTransactions()
        }
    }

    override fun showEquipmentByPassDialog(equipmentId: Int) {
        val dialog = childFragmentManager.findFragmentByTag(EquipmentCardDialogByPass.TAG)
        if (dialog == null) {
            byPassEquipmentDialog.get().show(childFragmentManager, EquipmentCardDialogByPass.TAG,
                    onClickDefectList = { presenter.onDefectListClicked(equipmentId) },
                    onClickRegisterDefect = { presenter.onRegisterDefectClicked(equipmentId) },
                    equipmentId = equipmentId)
            childFragmentManager.executePendingTransactions()
        }
    }

    override fun handleNetworkStatus(isOnline: Boolean) {
        binding.isNetworkEnabled = isOnline
    }

    override fun handleNfcStatus(isEnabled: Boolean) {
        binding.isNfcEnabled = isEnabled
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

    override fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun closeFragment() {
        hideKeyboard()
        navController.popBackStack()
    }

    //show defects list
    override fun openEquipmentDeffects(equipmentId: Int) {
        val args = Bundle()
        args.apply {
            putString(Constants.KEY_ENTITY_ID_LIST, equipmentId.toString())
            putInt(Constants.KEY_ENTITY_TYPE, Constants.ENTITY_EQUIPMENT_LIST)
            putBoolean(Constants.KEY_COULD_EDIT, false)
        }
        navController.navigate(R.id.action_questionsFragment_to_defectsSearchResultFragment, args)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.releaseNfcStatusHandler(this)
        (activity as? MainActivity)?.releaseNetworkStatusHandler(this)
    }

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}