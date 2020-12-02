package com.tradedoubler

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText

fun EditText.inputText(onTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
            onTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            onTextChanged.invoke(s.toString())
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged.invoke(s.toString())
        }
    })

}

fun EditText.validate(message: String, validator: (String) -> Boolean) {
    this.inputText {
        this.error = if (validator(it)) null else message
    }
    this.error = if (validator(this.text.toString())) null else message
}


var EditText.value
    get() = this.text.toString()
    set(value) {
        this.setText(value)
    }

fun String.isValidEmail(): Boolean
        = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()