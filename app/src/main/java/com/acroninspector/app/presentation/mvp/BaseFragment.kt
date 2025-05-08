package com.acroninspector.app.presentation.mvp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.presentation.custom.listener.IOnBackPressed
import java.lang.ClassCastException
import java.lang.Exception

abstract class BaseFragment : MvpAppCompatFragment(), BaseView, IOnBackPressed {

    fun <T : ViewDataBinding> getDataBindingView(
            @LayoutRes layoutId: Int,
            inflater: LayoutInflater,
            container: ViewGroup?): T {
        return DataBindingUtil.inflate(inflater, layoutId, container, false)
    }

    fun RecyclerView.addAcronScrollListenerToRecycler(layoutManager: RecyclerView.LayoutManager,
                                                      handler: (Int) -> Unit) {
        this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val position = try {
                    (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                } catch (e: ClassCastException) {
                    (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
                }

                try {
                    val topOfItemPosition = layoutManager.findViewByPosition(position)?.top!!

                    if (position == 0 && topOfItemPosition >= 0) {
                        handler(View.INVISIBLE)
                    } else handler(View.VISIBLE)

                } catch (e: Exception) {
                    handler(View.INVISIBLE)
                }
            }
        })
    }

    override fun showToast(resourceId: Int) {
        Toast.makeText(context, resourceId, Toast.LENGTH_LONG).show()
    }

    override fun showToast(resourceId: Int, text: String) {
        Toast.makeText(context, getString(resourceId, text), Toast.LENGTH_LONG).show()
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed(): Boolean {
        return false
    }
}