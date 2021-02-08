package com.irpn.base.extensions

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat



private fun Context.getTypedValue(res: Int): TypedValue {
    return TypedValue().apply {
        resources.getValue(res, this, true)
    }
}

fun Context.getColorFromRes(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun Context.getDrawableFromRes(@DrawableRes drawableRes: Int) = ContextCompat.getDrawable(this, drawableRes)

fun Context.getDimenFromRes(@DimenRes res: Int) = getTypedValue(res).float

fun Context.getPixelDimenFromRes(@DimenRes res: Int) = resources.getDimension(res)

fun Context.getDPIDimenFromRes(@DimenRes res: Int) = getPixelDimenFromRes(res) / resources.displayMetrics.density

fun Context.dipToPixels(dipValue: Float): Int =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, resources.displayMetrics).toInt()

fun pixelsToDp(px: Int): Int {
    return (px / Resources.getSystem().displayMetrics.density).toInt()
}

fun Context.spToPixels(value: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, resources.displayMetrics)

fun Context.showLongToast(str: String) = Toast.makeText(this, str, Toast.LENGTH_LONG).show()
fun Context.showShortToast(str: String) = Toast.makeText(this, str, Toast.LENGTH_SHORT).show()

