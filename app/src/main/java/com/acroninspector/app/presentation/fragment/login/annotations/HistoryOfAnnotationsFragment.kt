package com.acroninspector.app.presentation.fragment.annotations

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.acroninspector.app.App
import com.acroninspector.app.R
import com.acroninspector.app.domain.entity.local.display.DisplayAnnotation
import com.acroninspector.app.presentation.adapter.annotations.HistoryOfAnnotationAdapter
import com.acroninspector.app.presentation.fragment.login.annotations.HistoryOfAnnotationsPresenter
import com.acroninspector.app.presentation.fragment.login.annotations.HistoryOfAnnotationsView
import com.acroninspector.app.presentation.fragment.login.annotations.OnAnnotationClickListener
import com.acroninspector.app.presentation.fragment.login.base.BaseLoginFragment
import com.acroninspector.app.presentation.fragment.login.base.BaseLoginPresenter
import com.acroninspector.app.presentation.fragment.login.userfunction.UserFunctionFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_history_of_annotations.buttonUserFunction
import kotlinx.android.synthetic.main.fragment_history_of_annotations.progressBar
import kotlinx.android.synthetic.main.fragment_history_of_annotations.recyclerViewAnnotations
import kotlinx.android.synthetic.main.fragment_history_of_annotations.rootView
import kotlinx.android.synthetic.main.fragment_history_of_annotations.textViewAppVersion
import kotlinx.android.synthetic.main.fragment_history_of_annotations.textViewDetails
import kotlinx.android.synthetic.main.fragment_history_of_annotations.textViewReleses
import javax.inject.Inject

class HistoryOfAnnotationsFragment : BaseLoginFragment(), HistoryOfAnnotationsView,
    OnAnnotationClickListener {

    @Inject
    @InjectPresenter
    lateinit var presenter: HistoryOfAnnotationsPresenter

    @ProvidePresenter
    fun providePresenter(): HistoryOfAnnotationsPresenter = presenter

    private lateinit var adapter: HistoryOfAnnotationAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(context).componentsHolder
            .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history_of_annotations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonUserFunction.setOnClickListener {
            val userFunctionFragment = UserFunctionFragment()

            activity!!.supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.nothing, R.anim.fade_out)
                .replace(R.id.fragmentContainer, userFunctionFragment)
                .commit()
        }

        recyclerViewAnnotations.layoutManager = LinearLayoutManager(context)
    }

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
            .releaseComponent(javaClass)
    }

    override fun openFunctionFragment() {
        TODO("Not yet implemented")
    }

    override fun showAnnotations(annotation: List<DisplayAnnotation>) {
        val mutableAnnotations = annotation.toMutableList()

        if (!::adapter.isInitialized) {
            adapter = HistoryOfAnnotationAdapter(mutableAnnotations, this)
            recyclerViewAnnotations.adapter = adapter
        } else {
            adapter.updateAnnotations(annotation)
        }

//        adapter = HistoryOfAnnotationAdapter(annotation)
//        recyclerViewAnnotations.adapter = adapter
    }

    override fun displayReleaseDetails(details: String) {
        textViewDetails.text = details
    }

    override fun onAnnotationClick(realeaseId: String, dateteTime: String) {
        val date = dateteTime.split(" ")[0]
        presenter.loadDescriptionAnnotations(realeaseId)
        textViewReleses.text = String.format(getString(R.string.new_annotation_version), realeaseId, date)
    }

    override fun getRootView(): FrameLayout = rootView

    override fun getProgressBar(): ProgressBar = progressBar

    override fun getTextViewAppVersion(): AppCompatTextView = textViewAppVersion

    override fun getPresenter(): BaseLoginPresenter<*> = presenter

}