package com.irpn.base.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import jp.wasabeef.glide.transformations.RoundedCornersTransformation


fun AppCompatImageView.loadImage(uri: String) =
    Glide.with(this.context.applicationContext)
        .load(uri)
        .into(this)

fun ImageView.loadCornerImage(uri: String?, icPlaceholder: Int? = 0, radius: Int, margin: Int? = 0) =
    Glide.with(this.context.applicationContext)
        .load(uri)
        .transform(RoundedCornersTransformation(radius, margin!!))
        .placeholder(icPlaceholder!!)
        .into(this)

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) { }
    return false
}
