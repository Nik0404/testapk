package com.acroninspector.app.presentation.fragment.login.auth

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.lifecycleScope
import com.acroninspector.app.App
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.Constants.KEY_IS_AFTER_LOGOUT
import com.acroninspector.app.common.constants.PreferencesConstants
import com.acroninspector.app.presentation.custom.listener.ScanNfcListener
import com.acroninspector.app.presentation.fragment.login.base.BaseLoginFragment
import com.acroninspector.app.presentation.fragment.login.base.BaseLoginPresenter
import com.acroninspector.app.presentation.fragment.login.userfunction.UserFunctionFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_login.backButton
import kotlinx.android.synthetic.main.fragment_login.imageview_circle1
import kotlinx.android.synthetic.main.fragment_login.imageview_circle2
import kotlinx.android.synthetic.main.fragment_login.imageview_circle3
import kotlinx.android.synthetic.main.fragment_login.imageview_circle4
import kotlinx.android.synthetic.main.fragment_login.imageview_circle5
import kotlinx.android.synthetic.main.fragment_login.imageview_circle6
import kotlinx.android.synthetic.main.fragment_login.loginEditText
import kotlinx.android.synthetic.main.fragment_login.login_password_container
import kotlinx.android.synthetic.main.fragment_login.passwordEditText
import kotlinx.android.synthetic.main.fragment_login.pin_code_input
import kotlinx.android.synthetic.main.fragment_login.pin_input_layout
import kotlinx.android.synthetic.main.fragment_login.progressBar
import kotlinx.android.synthetic.main.fragment_login.rootView
import kotlinx.android.synthetic.main.fragment_login.signInButton
import kotlinx.android.synthetic.main.fragment_login.textViewAppVersion
import kotlinx.android.synthetic.main.fragment_login.textViewDeviceId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginFragment : BaseLoginFragment(), LoginView, ScanNfcListener {

    private lateinit var circles: Array<ImageView>

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        fun newInstance(isAfterLogout: Boolean): LoginFragment {
            val args = Bundle()
            args.putBoolean(KEY_IS_AFTER_LOGOUT, isAfterLogout)

            val fragment = LoginFragment()
            fragment.arguments = args

            return fragment
        }
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: LoginPresenter

    @ProvidePresenter
    fun providePresenter(): LoginPresenter = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(
            PreferencesConstants.APP_PREFERENCES,
            Context.MODE_PRIVATE
        )

        ensureDeviceId()
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
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginEditText.requestFocus()

        if (sharedPreferences.getBoolean(Constants.NFC_KEY, false)) {
            presenter.isPinLogin = true
            layoutPinVisible()
        }

        circles = arrayOf(
            imageview_circle1,
            imageview_circle2,
            imageview_circle3,
            imageview_circle4,
            imageview_circle5,
            imageview_circle6
        )

        circles.forEach { circle ->
            circle.setOnClickListener {
                pin_code_input.requestFocus()
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(pin_code_input, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        onInputPinCode()

        backButton.setOnClickListener {
            presenter.isPinLogin = false
            layoutLoginAndPasswordVisible()

            circles.forEach { c -> c.setImageResource(R.drawable.circle) }
            pin_code_input.setText(Constants.NO_ANSWER)
            loginEditText.setText(Constants.NO_ANSWER)
            sharedPreferences.edit().remove(Constants.NFC_KEY).commit()
        }

        signInButton.setOnClickListener {
            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()

            presenter.isChekedLoginAndPin(presenter.isPinLogin, login, password)
        }

        presenter.isAfterLogout = arguments!!.getBoolean(KEY_IS_AFTER_LOGOUT, false)
    }

    override fun hideKeyboard() {
        val inputMethodManager =
            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    override fun openUserFunctionsScreen() {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, UserFunctionFragment())
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

    private fun getTextViewDeviceId(): AppCompatTextView = textViewDeviceId

    override fun setDeviceId(deviceId: String) {
        getTextViewDeviceId().text = deviceId
    }

    private fun ensureDeviceId() {
        var deviceID = presenter.getDeviceId();
        if (deviceID.isNullOrEmpty()) {
            deviceID = Settings.Secure.getString(
                this.context!!.contentResolver,
                Settings.Secure.ANDROID_ID
            )
            presenter.setDeviceId(deviceID)
        }
    }

    override fun onNfcScanned(nfcCode: String) {
        loginEditText.setText(nfcCode)
        lifecycleScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                presenter.isPinLogin = true
                sharedPreferences.edit().putBoolean(Constants.NFC_KEY, true).apply()
                layoutPinVisible()
            }
        }
    }

    private fun layoutPinVisible() {
        pin_code_input.requestFocus()
        login_password_container.visibility = View.GONE
        pin_input_layout.visibility = View.VISIBLE
        backButton.visibility = View.VISIBLE
    }

    private fun layoutLoginAndPasswordVisible() {
        loginEditText.requestFocus()
        login_password_container.visibility = View.VISIBLE
        pin_input_layout.visibility = View.GONE
        backButton.visibility = View.GONE
    }

    private fun onInputPinCode() {
        pin_code_input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                updateCircles(s.length) // Обновление кружочков
                passwordEditText.setText(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun updateCircles(length: Int) {
        for (i in circles.indices) {
            circles[i].setImageResource(if (i < length) R.drawable.back_circle else R.drawable.circle)
        }
    }
}