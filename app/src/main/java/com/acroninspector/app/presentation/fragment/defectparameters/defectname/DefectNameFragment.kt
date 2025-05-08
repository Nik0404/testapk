package com.acroninspector.app.presentation.fragment.defectparameters.defectname

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.acroninspector.app.App
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.constants.Constants.KEY_EQUIPMENT_CLASS_ID
import com.acroninspector.app.databinding.FragmentDefectNamesBinding
import com.acroninspector.app.domain.entity.local.display.DisplayDefect
import com.acroninspector.app.presentation.activity.main.MainActivity
import com.acroninspector.app.presentation.adapter.defectnames.DefectNamesAdapter
import com.acroninspector.app.presentation.fragment.defectparameters.DefectParametersFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DefectNameFragment : DefectParametersFragment(), DefectNameView {

    private lateinit var binding: FragmentDefectNamesBinding

    @Inject
    lateinit var adapter: DefectNamesAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: DefectNamePresenter

    @ProvidePresenter
    fun providePresenter(): DefectNamePresenter = presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBindingView(com.acroninspector.app.R.layout.fragment_defect_names, inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerDefectNames.layoutManager = LinearLayoutManager(context)
        binding.recyclerDefectNames.adapter = adapter

        adapter.setOnItemClickListener { presenter.onDefectNameClicked(it) }

        binding.btnBack.onClick { handleOnClickBack() }
        binding.fabSearch.onClick { handleOnClickSearch() }

        searchDisposable = getSearchObservable()
                .subscribe { presenter.searchDefectNames(it.toString().trim()) }

        presenter.equipmentClassId = arguments?.getInt(KEY_EQUIPMENT_CLASS_ID, DEFAULT_INVALID_ID)!!
    }

    private fun getSearchObservable(): Observable<CharSequence> {
        return RxTextView.textChanges(binding.etSearch)
                .skipInitialValue()
                .subscribeOn(Schedulers.computation())
                .debounce(250, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun handleOnClickBack() {
        if (binding.etSearch.visibility == View.VISIBLE) {
            //Cancel searching
            hideKeyboard()

            binding.etSearch.visibility = View.GONE
            binding.etSearch.isEnabled = false
            binding.etSearch.setText("")

            Handler().postDelayed({
                binding.fabSearch.show()
            }, 250)

            binding.tvTitle.visibility = View.VISIBLE
        } else closeFragment()
    }

    private fun handleOnClickSearch() {
        if (binding.etSearch.visibility != View.VISIBLE) {
            //Start searching
            showKeyboard()

            binding.etSearch.visibility = View.VISIBLE
            binding.etSearch.isEnabled = true
            binding.etSearch.requestFocus()

            binding.fabSearch.hide()
            binding.tvTitle.visibility = View.INVISIBLE
        }
    }

    override fun passDefectNameId(id: Int) {
        (activity as? MainActivity)?.passDefectNameId(id)
        (activity as? MainActivity)?.releaseDefectNameListener()
        closeFragment()
    }

    override fun showSnackbar(resourceId: Int) {
        getSnackbar(resourceId).show()
    }

    override fun updateDefectNames(list: List<DisplayDefect>) = adapter.setData(list)

    override fun getEmptyStateView(): View = binding.emptyStateView

    override fun getProgressBar(): View = binding.progressBar

    override fun getRootView(): View = binding.root

    override fun onBackPressed(): Boolean {
        handleOnClickBack()
        return true
    }

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}