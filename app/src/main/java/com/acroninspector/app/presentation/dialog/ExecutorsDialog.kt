package com.acroninspector.app.presentation.dialog

import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.acroninspector.app.R
import com.acroninspector.app.databinding.DialogExecutorsBinding
import com.acroninspector.app.domain.entity.local.display.DisplayExecutor
import com.acroninspector.app.domain.interactors.executor.ExecutorInteractor
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.singleLine
import timber.log.Timber

class ExecutorsDialog(
        private val interactor: ExecutorInteractor
) : DialogFragment() {

    private lateinit var binding: DialogExecutorsBinding

    private lateinit var executorsDisposable: Disposable

    private lateinit var clickListener: (Int) -> Unit

    private var executorGroup: Int = 0

    private var executorId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_Acron_PopupWithPadding)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_executors, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCloseDialog.onClick { dismiss() }
        binding.btnCancel.onClick { dismiss() }

        binding.btnApply.onClick {
            val checkedExecutorId = binding.executorsGroup.checkedRadioButtonId
            if (checkedExecutorId != -1) {
                clickListener(checkedExecutorId)
                dismiss()
            } else dismiss()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        executorsDisposable = interactor
                .getExecutorsByGroup(executorGroup)
                .subscribe({
                    showExecutors(it)
                }, {
                    Timber.e(it)
                    dismiss()
                })
    }

    private fun showExecutors(executors: List<DisplayExecutor>) {
        val rbPaddingVertical = resources.getDimension(R.dimen.spacing_m).toInt()
        val rbPaddingLeft = resources.getDimension(R.dimen.spacing_xs2).toInt()
        val rbTextSize = resources.getDimension(R.dimen.text_size_xs2)
        val rbTextColor = resources.getColor(R.color.colorGray, null)

        for (executor in executors) {
            val rb = RadioButton(context).apply {
                id = executor.id
                text = executor.fullName
                tag = executor.id
                layoutParams = RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.WRAP_CONTENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT
                )
                singleLine = true
                setTextSize(TypedValue.COMPLEX_UNIT_PX, rbTextSize)
                setTextColor(rbTextColor)
                setPadding(rbPaddingLeft, rbPaddingVertical, 0, rbPaddingVertical)
                typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                ellipsize = TextUtils.TruncateAt.END
            }

            binding.executorsGroup.addView(rb)
        }

        binding.executorsGroup.check(executorId)
    }

    fun show(onClickExecutorListener: (Int) -> Unit, executorGroup: Int, executorId: Int,
             manager: FragmentManager?, tag: String?) {
        this.clickListener = onClickExecutorListener
        this.executorGroup = executorGroup
        this.executorId = executorId
        show(manager!!, tag)
    }

    override fun onDetach() {
        super.onDetach()
        try {
            executorsDisposable.dispose()
        } catch (e: UninitializedPropertyAccessException) {
            Timber.e(e)
        }
    }

    companion object {
        const val TAG = "Acron.Tag.ExecutorsDialog"
    }
}