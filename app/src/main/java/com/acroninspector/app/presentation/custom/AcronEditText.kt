package com.acroninspector.app.presentation.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import com.acroninspector.app.R
import com.acroninspector.app.common.extension.fractionLengthMoreThan
import com.acroninspector.app.common.extension.getWholePartLength
import com.acroninspector.app.common.extension.shortenFraction
import com.acroninspector.app.common.extension.shortenWholePart
import com.acroninspector.app.common.extension.wholePartLengthMoreThan

class AcronEditText : AppCompatEditText {

    private var symbolsBeforeComma: Int = getDefaultSymbolsBeforeComma()

    private var symbolsAfterComma: Int = getDefaultSymbolsAfterComma()

    constructor(context: Context) : super(context) {
        initView(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AcronEditText, 0, 0)

            symbolsBeforeComma = typedArray.getInt(
                R.styleable.AcronEditText_symbolsBeforeComma,
                getDefaultSymbolsBeforeComma()
            )
            symbolsAfterComma = typedArray.getInt(
                R.styleable.AcronEditText_symbolsAfterComma,
                getDefaultSymbolsAfterComma()
            )

            typedArray.recycle()
        }

        this.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                this.clearFocus()
            }
            false
        }
    }

    fun setSimbolsAfterComma(count: Int) {
        symbolsAfterComma = count
    }

    fun addOnNumberChangeListener(onTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(editable: Editable?) {
                val number = editable.toString()
                when {
                    wholePartLengthIsTooLong(number) -> shortenWholePart(number)
                    fractionLengthIsTooLong(number) -> shortenFraction(number)
                    else -> onTextChanged(number)
                }
            }
        })
    }

    private fun wholePartLengthIsTooLong(number: String): Boolean {
        return number.isNotEmpty() && number.wholePartLengthMoreThan(symbolsBeforeComma)
    }

    private fun fractionLengthIsTooLong(number: String): Boolean {
        return number.isNotEmpty() && number.fractionLengthMoreThan(symbolsAfterComma)
    }

    private fun shortenWholePart(number: String) {
        val selectionIndex = this.selectionEnd - 1
        val shortenNumber = number.shortenWholePart(selectionIndex, symbolsBeforeComma)
        this.setText(shortenNumber)

        if (number.getWholePartLength() > symbolsBeforeComma + 1) {
            this.setSelection(selectionIndex + 1)
        } else this.setSelection(selectionIndex)
    }

    private fun shortenFraction(number: String) {
        val selectionIndex = this.selectionEnd - 1
        val shortenNumber = number.shortenFraction(selectionIndex)
        this.setText(shortenNumber)

        this.setSelection(selectionIndex)
    }

    private fun getDefaultSymbolsBeforeComma() = DEFAULT_SYMBOLS_BEFORE_COMMA

    private fun getDefaultSymbolsAfterComma() = DEFAULT_SYMBOLS_AFTER_COMMA

    companion object {
        private const val DEFAULT_SYMBOLS_BEFORE_COMMA = 6
        private const val DEFAULT_SYMBOLS_AFTER_COMMA = 4
    }
}