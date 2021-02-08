package com.irpn.base.extension

import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatEditText

inline fun AppCompatEditText.aftertextChanged(crossinline listener: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            listener(s.toString())
        }

    })
}