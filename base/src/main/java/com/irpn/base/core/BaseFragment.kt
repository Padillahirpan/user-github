package com.irpn.base.core

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer

abstract class BaseFragment<VM : BaseViewModel> : PlainFragment() {

    abstract val viewModel: VM
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.doOnViewAttached()
        //setObserver()
    }

    private fun setObserver() {

    }
}