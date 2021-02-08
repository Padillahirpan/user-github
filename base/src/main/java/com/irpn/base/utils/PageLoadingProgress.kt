package com.irpn.base.utils

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.contains
import androidx.core.view.minusAssign
import androidx.core.view.plusAssign
import com.irpn.base.R

class PageLoadingProgress : LinearLayout {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_page_loading_progress, this, true)
        isClickable = true
        isFocusable = true
        isFocusableInTouchMode = true
    }

    fun showFromActivity(activity: AppCompatActivity) {
        val rootView = getViewGroup(activity)

        if (!rootView.contains(this)) rootView += this
    }

    fun hideFromActivity(activity: AppCompatActivity) {
        val rootView = getViewGroup(activity)

        if (rootView.contains(this)) rootView -= this
    }

    fun isVisible(activity: AppCompatActivity) = getViewGroup(activity).contains(this)

    private fun getViewGroup(activity: AppCompatActivity): ViewGroup {
        return activity.window.decorView.findViewById(android.R.id.content) as ViewGroup
    }

}