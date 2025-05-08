package com.acroninspector.app.presentation.fragment.defects.searchresult

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.acroninspector.app.App
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_TYPE
import com.acroninspector.app.common.constants.Constants.KEY_COULD_EDIT
import com.acroninspector.app.common.constants.Constants.KEY_ENTITY_ID
import com.acroninspector.app.common.constants.Constants.KEY_ENTITY_ID_LIST
import com.acroninspector.app.common.constants.Constants.KEY_ENTITY_TYPE
import com.acroninspector.app.common.constants.Constants.KEY_SEARCH_QUERY
import com.acroninspector.app.databinding.FragmentDefectsSearchResultBinding
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import com.acroninspector.app.presentation.adapter.defects.DefectsAdapter
import com.acroninspector.app.presentation.fragment.defects.DefectsBaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import timber.log.Timber
import javax.inject.Inject

class DefectsSearchResultFragment : DefectsBaseFragment(), DefectsSearchResultView {

    private lateinit var binding: FragmentDefectsSearchResultBinding

    private lateinit var navController: NavController

    @Inject
    lateinit var defectsAdapter: DefectsAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: DefectsSearchResultPresenter

    @ProvidePresenter
    fun providePresenter(): DefectsSearchResultPresenter = presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = getDataBindingView(R.layout.fragment_defects_search_result, inflater, container)
        binding.swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.colorBlue),
            ContextCompat.getColor(requireContext(), R.color.colorPurple),
            ContextCompat.getColor(requireContext(), R.color.colorGreen)
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { navController.popBackStack() }

        val searchQuery = arguments?.getString(KEY_SEARCH_QUERY, "")!!
        if (searchQuery.isNotEmpty()) {
            binding.tvTitle.text = searchQuery
            presenter.searchQuery = searchQuery
        }

        arguments?.getInt(KEY_ENTITY_TYPE, DEFAULT_INVALID_TYPE)?.let {
            presenter.entityType = it
        }
        arguments?.getInt(KEY_ENTITY_ID, DEFAULT_INVALID_ID)?.let {
            presenter.entityId = it
        }
        arguments?.getString(KEY_ENTITY_ID_LIST, "")?.let {
            presenter.entityIdList = it
        }
        arguments?.getBoolean(KEY_COULD_EDIT, true)?.let {
            presenter.defectCouldEdit = it
        }


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

    override fun openDefectDetails(defect: DisplayDefectLog) {
        val args = Bundle()
        args.putParcelable(Constants.KEY_DEFECT_OBJECT, defect)
        args.putBoolean(KEY_COULD_EDIT, arguments?.getBoolean(KEY_COULD_EDIT, true) ?: true)

        try {
            navController.navigate(R.id.action_defectsSearchResultFragment_to_defectDetailsFragment, args)
        } catch (e: IllegalArgumentException) {
            Timber.e(e)
        }
    }

    override fun setEquipmentEmptyState(@StringRes emptyStateMessageResId: Int) {
        prepareEmptyState(emptyStateMessageResId, R.drawable.ic_defects_empty_state)
    }

    override fun setSearchEmptyState(@StringRes emptyStateMessageResId: Int) {
        prepareEmptyState(emptyStateMessageResId, R.drawable.ic_search_empty_state)
    }

    private fun prepareEmptyState(@StringRes textResource: Int, @DrawableRes drawableTopResource: Int) {
        binding.emptyStateView.text = getString(textResource)

        val drawable = ContextCompat.getDrawable(requireContext(), drawableTopResource)
        binding.emptyStateView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
    }

    override fun getAccountButton(): View = binding.btnAccount

    override fun getNfcButton(): AppCompatImageButton = binding.btnNfc

    override fun getAppBarUnderlineView(): View = binding.appBarUnderline

    override fun getRecyclerView(): RecyclerView = binding.recyclerDefects

    override fun getEmptyStateView(): View = binding.emptyStateView

    override fun getSwipeRefreshLayout(): SwipeRefreshLayout = binding.swipeRefreshLayout

    override fun getBaseDefectsAdapter(): DefectsAdapter = defectsAdapter

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}