package com.tradedoubler

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import java.util.concurrent.ThreadLocalRandom

var EditText.value
    get() = this.text.toString()
    set(value) {
        this.setText(value)
    }

fun String.isValidEmail(): Boolean
        = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun randomInt() :String{
    val randomInteger = ThreadLocalRandom.current().nextInt(0,100000)
   return randomInteger.toString()
}
private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

 fun generateId(length: Int): String{
    return (1..length)
        .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}