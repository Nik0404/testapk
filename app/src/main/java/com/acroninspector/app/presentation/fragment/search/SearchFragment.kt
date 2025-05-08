package com.acroninspector.app.presentation.fragment.search

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DimenRes
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.acroninspector.app.App
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_TYPE
import com.acroninspector.app.common.constants.Constants.ENTITY_DEFECT_LOG
import com.acroninspector.app.common.constants.Constants.ENTITY_EQUIPMENT
import com.acroninspector.app.common.constants.Constants.KEY_IS_FROM_SEARCH
import com.acroninspector.app.common.constants.Constants.KEY_SEARCH_ENTITY
import com.acroninspector.app.common.constants.Constants.KEY_SEARCH_QUERY
import com.acroninspector.app.common.extension.addAcronTextChangedListener
import com.acroninspector.app.databinding.FragmentSearchBinding
import com.acroninspector.app.domain.entity.local.display.DisplaySearchHistory
import com.acroninspector.app.presentation.activity.main.MainView
import com.acroninspector.app.presentation.adapter.search.SearchAdapter
import com.acroninspector.app.presentation.mvp.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchFragment : BaseFragment(), SearchView {

    private lateinit var binding: FragmentSearchBinding

    private lateinit var navController: NavController

    private var searchDisposable: Disposable? = null

    @Inject
    lateinit var adapter: SearchAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: SearchPresenter

    @ProvidePresenter
    fun providePresenter(): SearchPresenter = presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBindingView(R.layout.fragment_search, inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showKeyboard()
        binding.etSearch.requestFocus()

        binding.recyclerSearchHistory.layoutManager = LinearLayoutManager(context)
        binding.recyclerSearchHistory.adapter = adapter

        adapter.setOnItemClickListener { presenter.onSearchHistoryItemClicked(it) }

        binding.btnBack.setOnClickListener { closeFragment() }
        binding.btnSearch.setOnClickListener {
            val searchQuery = binding.etSearch.text.toString()
            presenter.onSearchButtonClicked(searchQuery)
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.btnSearch.callOnClick()
            }
            false
        }

        binding.etSearch.addAcronTextChangedListener { text ->
            if (text.isEmpty()) {
                setEditTextSearchTextSize(editTextSearchHintSize)
            } else setEditTextSearchTextSize(R.dimen.activity_search_et_search_text_size_large)
        }

        searchDisposable = RxTextView.textChanges(binding.etSearch)
                .skipInitialValue()
                .subscribeOn(Schedulers.computation())
                .debounce(250, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    handleSearchButton(it.toString().trim())
                    presenter.search(it.toString().trim())
                }

        val searchEntity = arguments?.getInt(KEY_SEARCH_ENTITY, DEFAULT_INVALID_TYPE)!!
        presenter.searchEntity = searchEntity

        prepareSearchHint(searchEntity)
    }

    override fun setSearchQuery(searchQuery: String) {
        binding.etSearch.setText(searchQuery)
    }

    private var editTextSearchHintSize = R.dimen.activity_search_et_search_text_size_small

    private fun prepareSearchHint(searchEntity: Int) {
        when (searchEntity) {
            ENTITY_DEFECT_LOG -> {
                binding.etSearch.hint = getString(R.string.search_defect_hint)
                editTextSearchHintSize = R.dimen.activity_search_et_search_text_size_large
            }
            ENTITY_EQUIPMENT -> {
                binding.etSearch.hint = getString(R.string.search_equipment_hint)
                editTextSearchHintSize = R.dimen.activity_search_et_search_text_size_small
            }
        }
        setEditTextSearchTextSize(editTextSearchHintSize)
    }

    private fun setEditTextSearchTextSize(@DimenRes sizeRes: Int) {
        binding.etSearch.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(sizeRes))
    }

    private fun handleSearchButton(searchText: String) {
        if (searchText.isEmpty()) {
            binding.btnSearch.visibility = View.INVISIBLE
        } else binding.btnSearch.visibility = View.VISIBLE
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(binding.root)
    }

    override fun openDefectsFragment(searchQuery: String) {
        hideKeyboard()

        val args = Bundle()
        args.putString(KEY_SEARCH_QUERY, searchQuery)

        navController.navigate(R.id.action_searchFragment_to_defectsSearchResultFragment, args)
    }

    override fun openEquipmentsFragment(searchQuery: String) {
        hideKeyboard()

        val args = Bundle()
        args.putString(KEY_SEARCH_QUERY, searchQuery)
        args.putBoolean(KEY_IS_FROM_SEARCH, true)

        navController.navigate(R.id.action_searchFragment_to_nestedEquipmentFragment, args)
    }

    override fun updateSearchHistory(searchHistory: List<DisplaySearchHistory>) {
        adapter.setData(searchHistory)
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
        if (searchDisposable != null && !searchDisposable!!.isDisposed) {
            searchDisposable!!.dispose()
        }

        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}