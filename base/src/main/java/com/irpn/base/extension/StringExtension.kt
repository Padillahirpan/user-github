package com.irpn.base.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.text.DecimalFormat


fun String.toRupiah(): String {
    val value = java.lang.Double.parseDouble(this)
    val formatter = DecimalFormat("###,###,###")
    return "Rp ${formatter.format(value).replace(",", ".")}"
}