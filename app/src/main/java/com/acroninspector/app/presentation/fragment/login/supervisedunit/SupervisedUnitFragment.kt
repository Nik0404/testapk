package com.acroninspector.app.presentation.fragment.login.supervisedunit

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatTextView
import com.acroninspector.app.App
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.domain.entity.local.database.Division
import com.acroninspector.app.presentation.fragment.login.base.BaseLoginFragment
import com.acroninspector.app.presentation.fragment.login.base.BaseLoginPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_supervised_unit.chooseSupervisedUnitButton
import kotlinx.android.synthetic.main.fragment_supervised_unit.progressBar
import kotlinx.android.synthetic.main.fragment_supervised_unit.rootView
import kotlinx.android.synthetic.main.fragment_supervised_unit.textViewAppVersion
import kotlinx.android.synthetic.main.fragment_supervised_unit.unitsGroup
import javax.inject.Inject

class SupervisedUnitFragment : BaseLoginFragment(), SupervisedUnitView {

    @Inject
    @InjectPresenter
    lateinit var presenter: SupervisedUnitPresenter

    @ProvidePresenter
    fun providePresenter(): SupervisedUnitPresenter = presenter

    private var selectedDivisionId = Constants.DEFAULT_INVALID_ID

    private val rbClickListener = View.OnClickListener {
        selectedDivisionId = it.tag as Int
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(context).componentsHolder
            .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_supervised_unit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chooseSupervisedUnitButton.setOnClickListener {
            presenter.selectDivisionButtonClicked(selectedDivisionId)
        }
    }

    override fun showDivisions(divisions: List<Division>) {
        val rbPadding = resources.getDimension(R.dimen.spacing_m).toInt()
        val rbTextSize = resources.getDimension(R.dimen.text_size_xs)
        val rbTextColor = resources.getColor(R.color.colorText, null)

        for (division in divisions) {
            val rb = RadioButton(context).apply {
                id = division.id
                text = division.id.toString()
                tag = division.id
                layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                )
                maxLines = 1
                setTextSize(TypedValue.COMPLEX_UNIT_PX, rbTextSize)
                setTextColor(rbTextColor)
                setPadding(0, rbPadding, 0, rbPadding)
                setOnClickListener(rbClickListener)
            }

            unitsGroup.addView(rb)
        }

        unitsGroup.check(divisions[0].id)
        selectedDivisionId = divisions[0].id
    }

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
            .releaseComponent(javaClass)
    }

    override fun getRootView(): FrameLayout = rootView

    override fun getProgressBar(): ProgressBar = progressBar

    override fun getTextViewAppVersion(): AppCompatTextView = textViewAppVersion

    override fun getPresenter(): BaseLoginPresenter<*> = presenter
}
