package com.acroninspector.app.presentation.mvp

import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity : MvpAppCompatActivity(), BaseView {

    fun <T : ViewDataBinding> getDataBindingView(@LayoutRes layoutId: Int): T {
        return DataBindingUtil.setContentView(this, layoutId)
    }

    override fun showToast(resourceId: Int) {
        Toast.makeText(this, resourceId, Toast.LENGTH_LONG).show()
    }

    override fun showToast(resourceId: Int, text: String) {
        Toast.makeText(this, getString(resourceId, text), Toast.LENGTH_LONG).show()
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}