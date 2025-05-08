package com.acroninspector.app.presentation.fragment.login.userfunction

import android.content.Context
import android.graphics.Paint.UNDERLINE_TEXT_FLAG
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.acroninspector.app.App
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.domain.entity.local.display.DisplayUserFunction
import com.acroninspector.app.presentation.fragment.annotations.HistoryOfAnnotationsFragment
import com.acroninspector.app.presentation.fragment.login.auth.LoginFragment
import com.acroninspector.app.presentation.fragment.login.base.BaseLoginFragment
import com.acroninspector.app.presentation.fragment.login.base.BaseLoginPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_user_function.chooseFunctionButton
import kotlinx.android.synthetic.main.fragment_user_function.exitButton
import kotlinx.android.synthetic.main.fragment_user_function.functionsGroup
import kotlinx.android.synthetic.main.fragment_user_function.imageButtonNotification
import kotlinx.android.synthetic.main.fragment_user_function.progressBar
import kotlinx.android.synthetic.main.fragment_user_function.rootView
import kotlinx.android.synthetic.main.fragment_user_function.textViewAppVersion
import org.jetbrains.anko.singleLine
import javax.inject.Inject

class UserFunctionFragment : BaseLoginFragment(), UserFunctionView {

    @Inject
    @InjectPresenter
    lateinit var presenter: UserFunctionPresenter

    @ProvidePresenter
    fun providePresenter(): UserFunctionPresenter = presenter

    private var selectedFunctionCode = Constants.DEFAULT_INVALID_ID

    private val rbClickListener = View.OnClickListener {
        selectedFunctionCode = it.tag as Int
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(context).componentsHolder
            .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_function, container, false)
        val button = view.findViewById<AppCompatButton>(R.id.exitButton)
        button.paintFlags = button.paintFlags or UNDERLINE_TEXT_FLAG

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageButtonNotification.setOnClickListener {
            presenter.openButtonAnnotation()
        }

        chooseFunctionButton.setOnClickListener {
            presenter.selectFunctionButtonClicked(selectedFunctionCode)
        }

        exitButton.setOnClickListener {
            presenter.exitButtonClicked()
        }
    }

    override fun showFunctions(functions: List<DisplayUserFunction>) {
        val rbPadding = resources.getDimension(R.dimen.spacing_m).toInt()
        val rbTextSize = resources.getDimension(R.dimen.text_size_xs)
        val rbTextColor = resources.getColor(R.color.colorText, null)

        for (function in functions) {
            val rb = RadioButton(context).apply {
                id = function.code
                text = getString(function.title)
                tag = function.code
                layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                )
                singleLine = true
                setTextSize(TypedValue.COMPLEX_UNIT_PX, rbTextSize)
                setTextColor(rbTextColor)
                setPadding(0, rbPadding, 0, rbPadding)
                setOnClickListener(rbClickListener)
            }

            functionsGroup.addView(rb)
        }

        functionsGroup.check(functions[0].code)
        selectedFunctionCode = functions[0].code
    }

    override fun openLoginFragment() {
        activity!!.supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.nothing, R.anim.fade_out)
            .replace(R.id.fragmentContainer, LoginFragment.newInstance(false))
            .commit()
    }

    override fun openHistoryOfAnnotationsFragment() {
        activity!!.supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.nothing, R.anim.fade_out)
            .replace(R.id.fragmentContainer, HistoryOfAnnotationsFragment())
            .commit()
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

