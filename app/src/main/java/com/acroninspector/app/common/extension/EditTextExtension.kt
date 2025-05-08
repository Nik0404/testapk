package com.acroninspector.app.common.extension

import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatEditText

inline fun AppCompatEditText.addAcronTextChangedListener(crossinline onTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(text.toString())
        }
    })
}