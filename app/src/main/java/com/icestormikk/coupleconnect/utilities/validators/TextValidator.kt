package com.icestormikk.coupleconnect.utilities.validators

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

abstract class TextValidator(private val textView: TextView) : TextWatcher {
    abstract fun validate(textView: TextView?, text: String?)
    override fun afterTextChanged(s: Editable) {
        val text = textView.text.toString()
        validate(textView, text)
    }

    override fun beforeTextChanged(
        s: CharSequence, start: Int, count: Int, after: Int
    ) {}

    override fun onTextChanged(
        s: CharSequence, start: Int, before: Int, count: Int
    ) {}
}