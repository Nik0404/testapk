package com.acroninspector.app.presentation.fragment.defects.defectlogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.acroninspector.app.App
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.ENTITY_DEFECT_LOG
import com.acroninspector.app.common.constants.Constants.KEY_DEFECT_OBJECT
import com.acroninspector.app.common.constants.Constants.KEY_SEARCH_ENTITY
import com.acroninspector.app.databinding.FragmentDefectLogsBinding
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import com.acroninspector.app.presentation.activity.main.MainActivity
import com.acroninspector.app.presentation.activity.main.MainView
import com.acroninspector.app.presentation.adapter.defects.DefectsAdapter
import com.acroninspector.app.presentation.custom.listener.NotificationsCounterHandler
import com.acroninspector.app.presentation.fragment.defects.DefectsBaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.sdk27.coroutines.onClick
import timber.log.Timber
import javax.inject.Inject

class DefectLogsFragment : DefectsBaseFragment(), DefectLogsView, NotificationsCounterHandler {

    private lateinit var binding: FragmentDefectLogsBinding

    private lateinit var navController: NavController

    @Inject
    lateinit var defectsAdapter: DefectsAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: DefectLogsPresenter

    @ProvidePresenter
    fun providePresenter(): DefectLogsPresenter = presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainView)?.unlockDrawer()
    }

    override fun onStart() {
        super.onStart()
        (activity as? MainActivity)?.setNotificationsCounterHandler(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBindingView(R.layout.fragment_defect_logs, inflater, container)
        binding.swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(context!!, R.color.colorBlue),
                ContextCompat.getColor(context!!, R.color.colorPurple),
                ContextCompat.getColor(context!!, R.color.colorGreen)
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnMenu.onClick { (activity as? MainView)?.openDrawer() }
        binding.fabSearch.onClick { openSearchFragment() }

        defectsAdapter.setOnItemClickListener { presenter.onDefectClicked(it) }
        getSwipeRefreshLayout().setOnRefreshListener { presenter.refreshDefects() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(binding.root)
    }

    override fun handleNetworkStatus(isOnline: Boolean) {
        binding.isNetworkEnabled = isOnline
    }

    override fun handleNfcStatus(isEnabled: Boolean) {
        binding.isNfcEnabled = isEnabled
    }

    override fun handleNotificationsCount(count: Int) {
        if (count > 0) {
            binding.counterNotifications.tvCount.visibility = View.VISIBLE
            binding.counterNotifications.tvCount.text = count.toString()
        } else binding.counterNotifications.tvCount.visibility = View.INVISIBLE
    }

    override fun openSearchFragment() {
        (activity as? MainView)?.lockDrawer()

        val args = Bundle()
        args.putInt(KEY_SEARCH_ENTITY, ENTITY_DEFECT_LOG)

        navController.navigate(R.id.action_defectsFragment_to_searchFragment, args)
    }

    override fun openDefectDetails(defect: DisplayDefectLog) {
        (activity as? MainView)?.lockDrawer()

        val args = Bundle()
        args.putParcelable(KEY_DEFECT_OBJECT, defect)

        try {
            navController.navigate(R.id.action_defectsFragment_to_defectDetailsFragment, args)
        } catch (e: IllegalArgumentException) {
            Timber.e(e)
        }
    }

    override fun getAccountButton(): View = binding.btnAccount

    override fun getNfcButton(): AppCompatImageButton = binding.btnNfc

    override fun getAppBarUnderlineView(): View = binding.appBarUnderline

    override fun getRecyclerView(): RecyclerView = binding.recyclerDefects

    override fun getEmptyStateView(): View = binding.emptyStateView

    override fun getSwipeRefreshLayout(): SwipeRefreshLayout = binding.swipeRefreshLayout

    override fun getBaseDefectsAdapter(): DefectsAdapter = defectsAdapter

    override fun onStop() {
        super.onStop()
        (activity as? MainActivity)?.releaseNotificationsCounterHandler(this)
    }

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}