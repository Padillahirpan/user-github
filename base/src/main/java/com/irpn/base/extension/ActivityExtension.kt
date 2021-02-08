package com.irpn.base.extension

import androidx.appcompat.app.AppCompatActivity
import com.irpn.base.utils.KeyboardUtil

fun AppCompatActivity.hideKeyboard() = KeyboardUtil.hideKeyboard(this)
fun AppCompatActivity.showKeyboard() = KeyboardUtil.showKeyboard(this)